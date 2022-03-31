package com.spring.sharepod.v1.controller;

import com.spring.sharepod.entity.ChatMessage;
import com.spring.sharepod.entity.ChatRoom;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.dto.request.ChatMessageRequestDto;
import com.spring.sharepod.v1.repository.ChatMessageRepository;
import com.spring.sharepod.v1.repository.ChatRoomRepository;
import com.spring.sharepod.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @MessageMapping("/templates/chat/message")  // 웹소켓으로 들어오는 메시지 발행 처리 -> 클라이언트에서는 /pub/chat/message로 발행 요청
    @Transactional
    public void message(ChatMessageRequestDto.Wirte message) {

        ChatRoom chatRoom = chatRoomRepository.findById(message.getChatRoomId()).orElseThrow(()-> new ErrorCodeException(ErrorCode.BOARD_BOARDQUILITY_NOT_EXIST));
        User user = userRepository.findById(message.getUserId()).orElseThrow(()-> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.getMessage())
                .chatRoom(chatRoom)
                .writer(user)
                .build();

        //Websocket에 발행된 메시지를 redis로 발행(publish)
        redisTemplate.convertAndSend(channelTopic.getTopic(),
                ChatMessageRequestDto.WirteSubscriber.builder()
                        .userId(message.getUserId())
                        .userNickname(user.getNickName())
                        .modifiedAt(message.getModifiedAt())
                        .chatRoomId(message.getChatRoomId())
                        .message(message.getMessage())
                        .build());

        //룸 modfiatedat 시간 변경하기
        Long chatMessageid = chatMessageRepository.save(chatMessage).getId();

        ChatMessage getmessage = chatMessageRepository.findById(chatMessageid).orElseThrow(()-> new ErrorCodeException(ErrorCode.CHATMESSAGE_NOT_EXIST));

        //chatRoom 수정시간 변경
        chatRoom.setModifiedAt(getmessage.getModifiedAt());
    }
}