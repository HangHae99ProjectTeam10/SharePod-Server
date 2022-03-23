package com.spring.sharepod.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.sharepod.v1.dto.request.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    //Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
    public void sendMessage(String publishMessage) {
        try {
            System.out.println("RedisSubscriber에서 보내는 메시지");
            System.out.println(publishMessage);
            // ChatMessage 객채로 맵핑
//            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            ChatMessageRequestDto.WirteSubscriber chatMessage = objectMapper.readValue(publishMessage, ChatMessageRequestDto.WirteSubscriber.class);


            System.out.println("sub 메시지 확인");
            System.out.println(chatMessage);

            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatRoomId() , chatMessage);
            System.out.println("발송 요청 완료");
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}
