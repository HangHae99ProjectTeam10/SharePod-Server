package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class NoticeResponseDto {


    @Getter
    @AllArgsConstructor
    @Builder
    public static class Notice {
        private Long noticeId;
        private String noticeName;
        private String userRegion;
        private LocalDateTime startRental;
        private LocalDateTime endRental;
        private String otherUserImg;
        private String noticeMsg;
    }
}
