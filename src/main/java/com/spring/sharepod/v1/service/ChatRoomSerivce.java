package com.spring.sharepod.v1.service;


import com.spring.sharepod.entity.*;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.dto.request.ChatRoomRequestDto;
import com.spring.sharepod.v1.dto.response.ChatRoomResponseDto;
import com.spring.sharepod.v1.repository.Board.BoardRepository;
import com.spring.sharepod.v1.repository.ChatMessage.ChatMessageRepository;
import com.spring.sharepod.v1.repository.ChatRoomRepository;
import com.spring.sharepod.v1.repository.Notice.NoticeRepository;
import com.spring.sharepod.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRoomSerivce {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;

    // 27번 채팅방 생성
    @Transactional
    public ChatRoomResponseDto.ChatRoomData createChatRoom(@RequestBody ChatRoomRequestDto.Create create) {
        Board board = boardRepository.findById(create.getBoardId()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND));
        User buyer = userRepository.findById(create.getBuyerId()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        //이미 있는지 확인
        ChatRoom chatRoomexistcehck = chatRoomRepository.findByBuyerAndAndBoard(buyer, board);
        if (chatRoomexistcehck != null) {
            ChatRoomResponseDto.ChatRoomData chatRoomData = ChatRoomResponseDto.ChatRoomData.builder()
                    .chatRoomId(chatRoomexistcehck.getId())
                    .build();
            return chatRoomData;
        }
        ChatRoom chatRoom = ChatRoom.create(ChatRoom.builder()
                .buyer(buyer)
                .seller(board.getUser())
                .board(board)
                .build());

        //chatRoom 정보 저장
        Long chatRoomId = chatRoomRepository.save(chatRoom).getId();
        char quotes = '"';
        noticeRepository.save(Notice.builder()
                .board(board)
                .receiver(board.getUser())
                .sender(buyer)
                .noticeInfo(quotes + board.getTitle() + quotes + "  채팅을 요청했습니다.")
                .isChat(true)
                .build());
        ChatRoomResponseDto.ChatRoomData chatRoomData = ChatRoomResponseDto.ChatRoomData.builder()
                .sellerNickName(chatRoom.getSeller().getNickName())
                .buyerNickName(chatRoom.getBuyer().getNickName())
                .sellerImg(chatRoom.getSeller().getUserImg())
                .boardTitle(chatRoom.getBoard().getTitle())
                .boardImg(chatRoom.getBoard().getImgFiles().getFirstImgUrl())
                .dailyRentalFee(chatRoom.getBoard().getAmount().getDailyRentalFee())
                .chatId(chatRoomId)
                .boardId(create.getBoardId())
                .buyerId(create.getBuyerId())
                .build();
        return chatRoomData;
    }

    // 28번 채팅 리스트 받아오기
    @Transactional
    public ChatRoomResponseDto.ChatRoomListData findChatList(Long userId) {
        //내 정보 가져오기
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByUserId(userId);
        List<ChatRoomResponseDto.ChatRoomList> chatRoomListList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            if (Objects.equals(userId, chatRoom.getBuyer().getId())) {
                String lastChat = "";
                if (chatRoom.getChatMessageList().size() == 0) {
                    lastChat = "채팅이 없습니다!";
                } else {
                    lastChat = chatRoom.getChatMessageList().get(chatRoom.getChatMessageList().size() - 1).getMessage();
                }
                ChatRoomResponseDto.ChatRoomList chatRoomListBuilder = ChatRoomResponseDto.ChatRoomList.builder()
                        .chatRoomId(chatRoom.getId())
                        .otherImg(chatRoom.getSeller().getUserImg())
                        .boardImg(chatRoom.getBoard().getImgFiles().getFirstImgUrl())
                        .boardTitle(chatRoom.getBoard().getTitle())
                        .otherNickName(chatRoom.getSeller().getNickName())
                        .otherRegion(chatRoom.getSeller().getUserRegion())
                        .dailyRentalFee(chatRoom.getBoard().getAmount().getDailyRentalFee())
                        .modifiedAt(chatRoom.getModifiedAt())
                        .lastChat(lastChat)
                        .build();
                chatRoomListList.add(chatRoomListBuilder);
            } else {
                String lastChat = "";
                if (chatRoom.getChatMessageList().size() == 0) {
                    lastChat = "채팅이 없습니다!";
                } else {
                    lastChat = chatRoom.getChatMessageList().get(chatRoom.getChatMessageList().size() - 1).getMessage();
                }
                ChatRoomResponseDto.ChatRoomList chatRoomListBuilder = ChatRoomResponseDto.ChatRoomList.builder()
                        .chatRoomId(chatRoom.getId())
                        .otherImg(chatRoom.getBuyer().getUserImg())
                        .boardImg(chatRoom.getBoard().getImgFiles().getFirstImgUrl())
                        .boardTitle(chatRoom.getBoard().getTitle())
                        .otherNickName(chatRoom.getBuyer().getNickName())
                        .otherRegion(chatRoom.getBuyer().getUserRegion())
                        .dailyRentalFee(chatRoom.getBoard().getAmount().getDailyRentalFee())
                        .modifiedAt(chatRoom.getModifiedAt())
                        .lastChat(lastChat)
                        .build();
                chatRoomListList.add(chatRoomListBuilder);
            }
        }
        return ChatRoomResponseDto.ChatRoomListData.builder()
                .result("success")
                .msg("전체 채팅 리스트 조회 성공")
                .myImg(user.getUserImg())
                .myNickname(user.getNickName())
                .chatRoomList(chatRoomListList)
                .build();
    }

    // 29번 해당 채팅방 채팅내용 반환
    public ChatRoomResponseDto.ChatMessageListData roomChatListService(Long userId, Long chatroomId, LocalDateTime localDateTime) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatroomId).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.CHATROOM_NOT_EXIST));
        User another = new User();

        //내가 buyer인지 seller인지 구별하기 위한 코드
        if (Objects.equals(chatRoom.getBuyer().getId(), userId)) {
            another = chatRoom.getSeller();
        } else {
            another = chatRoom.getBuyer();
        }

        LocalDateTime lastDateTime = null;
        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomOrderByModifiedAt(chatroomId,localDateTime);
        int resultCount = chatMessageList.size();

        if(resultCount>=1){
            lastDateTime = chatMessageList.get(resultCount-1).getModifiedAt();
        }

        List<ChatRoomResponseDto.ChatMessageData> chatMessageDataList = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessageList) {
            chatMessageDataList.add(ChatRoomResponseDto.ChatMessageData.builder()
                    .message(chatMessage.getMessage())
                    .userNickname(chatMessage.getWriter().getNickName())
                    .modifiedAt(chatMessage.getModifiedAt())
                    .build());
        }
        return ChatRoomResponseDto.ChatMessageListData.builder()
                .result("success")
                .msg("해당 채팅방 채팅내용 반환성공")
                .resultCount(resultCount)
                .lastDatetime(lastDateTime)
                .otherImg(another.getUserImg())
                .otherNickName(another.getNickName())
                .chatMessageDataList(chatMessageDataList)
                .build();
    }
}
