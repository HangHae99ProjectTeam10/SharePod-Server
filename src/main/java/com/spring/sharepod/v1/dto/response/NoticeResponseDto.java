package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class NoticeResponseDto {


    @Getter
    @AllArgsConstructor
    @Builder
    public static class Notice {
        private Long noticeId;
        private String noticeName;
        private String noticeMsg;

    }
}
