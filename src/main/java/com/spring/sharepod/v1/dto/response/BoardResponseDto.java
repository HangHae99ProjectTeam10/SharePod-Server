package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class BoardResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BoardWrite {
        private String result;
        private String msg;
        private BoardData boardData;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BoardData {

        private Long boardId;
        private String firstImgUrl;
        private String title;
        private String boardRegion;
        private int dailyRentalFee;
        private String boardTag;
        private String category;
        private LocalDateTime modifiedAt;

    }


    @Getter
    @AllArgsConstructor
    @Builder
    public static class BoardAll {

        private Long boardId;
        private String firstImgUrl;
        private String title;
        private String category;
        private int dailyRentalFee;
        private String boardRegion;
        private String boardTag;
        private LocalDateTime modifiedAt;
        private Boolean isLiked;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardDetail {
        private String Title;
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
        private String modifiedAt;
        private int likeCount;


    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MyBoard {
        private Long boardId;
        private String boardTitle;
        private String boardTag;
        private String FirstImg;
        private LocalDateTime modifiedAt;
        private int dailyRentalFee;
        private String nickName;
        private String category;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    public static class VideoAll {
        private Long boardId;
        private String Title;
        private String boardTag;
        private String videoUrl;
        private String userImg;
        private String nickName;
    }


}
