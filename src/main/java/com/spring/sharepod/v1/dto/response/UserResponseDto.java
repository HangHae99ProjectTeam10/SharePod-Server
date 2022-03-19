package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class UserResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Login {
        private String result;
        private String msg;
        private Long userId;
        private String nickName;
        private String userRegion;
        private String userImg;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class LoginReFreshToken {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        private Long userId;
        private String username;
        private String nickName;
        private String userRegion;
        private String userImg;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    public static class RentBuyer {
        private Long boardId;
        private String boardTitle;
        private String boardRegion;
        private String boardTag;
        private String FirstImgUrl;
        private int dailyRentalFee;
        private LocalDate startRental;
        private LocalDate endRental;
        private String nickName;
        private String category;
        private Long authId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RentSeller {
        private Long boardId;
        private String boardTitle;
        private String boardRegion;
        private String boardTag;
        private String FirstImgUrl;
        private int dailyRentalFee;
        private LocalDate startRental;
        private LocalDate endRental;
        private String nickName;
        private String category;
        private Long authId;
    }



}
