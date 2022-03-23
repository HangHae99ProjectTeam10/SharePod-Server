package com.spring.sharepod.v1.dto.request;

import lombok.*;

public class ChatRoomRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    public static class Create {
        private Long boardId;
        private Long buyerId;
    }

}
