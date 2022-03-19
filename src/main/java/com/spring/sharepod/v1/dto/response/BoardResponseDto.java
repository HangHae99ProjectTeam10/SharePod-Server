package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BoardResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BoardAll {

        private Long boardId;
        private String firstImgUrl;
        private String Title;
        private String category;
        private int dailyRentalFee;
        private String boardContents;
        private String boardTag;
        private String sellerImgUrl;
        private String sellerNickName;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardDetail {
        private String Title;
        private String videoUrl;
        private String firstImgUrl;
        private String secondImgUrl;
        private String lastImgUrl;
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
