package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatRoomData {
        private Long chatId;
        private String buyerNickName;
        private String sellerNickName;
        private String sellerImg;
        private String boardImg;
        private String boardTitle;
        private int dailyRentalFee;
        private Long boardId;
        private Long buyerId;

        private Long chatRoomId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatRoomList{
        private Long chatRoomId;
        private String otherImg;
        private String boardImg;
        private String otherNickName;
        private String lastChat;
        private String otherRegion;
        private LocalDateTime modifiedAt;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatRoomListData{
        private String result;
        private String msg;
        private String myImg;
        private String myNickname;
        private List<ChatRoomList> chatRoomList;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessageData{
        private String message;
        private String userNickname;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessageListData{
        private String result;
        private String msg;
        private String otherImg;
        private String otherNickName;
        private int resultCount;
        private List<ChatMessageData> chatMessageDataList;
    }
}