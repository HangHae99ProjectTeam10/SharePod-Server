package com.spring.sharepod.v1.dto.response.Liked;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Date;

public class LikedResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class LikedPost {
        private String result;
        private String msg;
        private Long userId;
        private Long boardId;

    }


}
