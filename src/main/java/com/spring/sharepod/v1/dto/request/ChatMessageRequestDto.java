package com.spring.sharepod.v1.dto.request;

import lombok.*;

public class ChatMessageRequestDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    public static class Wirte {
        private Long userId;
        private Long chatRoomId;
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    public static class WirteSubscriber {
        private Long userId;
        private String userNickname;
        private Long chatRoomId;
        private String message;
    }

}
