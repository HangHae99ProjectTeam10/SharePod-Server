package com.spring.sharepod.v1.dto.response.Board;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class BoardResponseDto {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class BoardWrite {
        private String result;
        private String msg;
        private Long userId;
        private BoardData boardData;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BoardModifiedData {
        private String result;
        private String msg;
        private BoardModifedDetail boardData;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BoardData {
        private Long boardId;
        private String contents;
        private String productQuality;
        private String firstImgUrl;
        private String title;
        private String boardRegion;
        private int dailyRentalFee;
        private int originalPrice;
        private String boardTag;
        private String category;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BoardDetail {
        private String title;
        private String videoUrl;
        private List<String> imgFiles;
        private String contents;
        private int originPrice;
        private int dailyRentalFee;
        private String boardTag;
        private String nickName;
        private String sellerRegion;
        private String sellerImg;
        private String boardRegion;
        private String category;
        private String boardQuaility;
        private boolean isLiked;
        private LocalDateTime modifiedAt;
        private int likeCount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MyBoard {
        private Long boardId;
        private String boardTitle;
        private String boardTag;
        private String boardRegion;
        private String FirstImg;
        private LocalDateTime modifiedAt;
        private int dailyRentalFee;
        private String nickName;
        private String category;
        private Boolean isLiked;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class VideoAll {
        private Long boardId;
        private String videoUrl;
        private String userImg;
        private String nickName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class BoardAllList {
        private String result;
        private String msg;
        private int resultCount;
        private List<BoardAllResponseDto> listData;
    }
}
