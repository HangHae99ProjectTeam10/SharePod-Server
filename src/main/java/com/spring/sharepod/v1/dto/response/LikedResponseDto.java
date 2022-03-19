package com.spring.sharepod.v1.dto.response;

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
    public static class Liked {
        private Long boardId;
        private String boardTitle;
        private String boardRegion;
        private String boardTag;
        private String FirstImg;
        private Boolean isliked;
        private LocalDateTime modifiedAt;
        private int dailyRentalFee;
        private String userNickName;
        private String category;
    }
}
