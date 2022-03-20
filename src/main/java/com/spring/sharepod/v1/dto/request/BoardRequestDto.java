package com.spring.sharepod.v1.dto.request;

import lombok.*;

public class BoardRequestDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    public static class PatchBoard {
        private Long userId;
        private String title;
        private String videoUrl;
        private String firstImgUrl;
        private String secondImgUrl;
        private String lastImgUrl;
        private String contents;
        private int originPrice;
        private int dailyRentalFee;
        private String boardRegion;
        private String category;
        private String productQuality;
        private String boardTag;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WriteBoard {
        private Long userId;
        private String title;
        private String contents;
        private String productQuality;
        private String boardRegion;
        private String boardTag;
        private int originPrice;
        private int dailyRentalFee;
        private String category;
        private String videoUrl;
        private String firstImgUrl;
        private String secondImgUrl;
        private String lastImgUrl;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getReels {
        private Long userId;
        private String title;
        private String contents;
        private String productQuality;
        private String boardRegion;
        private String boardTag;
        private int originPrice;
        private int dailyRentalFee;
        private String category;
        private String videoUrl;
        private String firstImgUrl;
        private String secondImgUrl;
        private String lastImgUrl;

    }





}
