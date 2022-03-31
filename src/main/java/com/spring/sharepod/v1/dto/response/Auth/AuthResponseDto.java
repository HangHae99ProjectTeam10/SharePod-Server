package com.spring.sharepod.v1.dto.response.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class AuthResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthDataAll {
        private String result;
        private String msg;
        private Long sellerId;
        private Long buyerId;
        private LocalDate startRental;
        private LocalDate endRental;
        private boolean authAllCheck;
        private List<AuthDataResponseDto> data;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthUploadDTO{
        private String result;
        private String msg;
        private Long userId;
        private Long authImgId;
        private String authImgUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthImgBoolDTO{
        private String result;
        private String msg;
        private Long authImgId;
        private Long sellerid;
        private boolean check;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthReUploadDTO{
        private String result;
        private String msg;
        private boolean authReupload;
        private Long sellerId;
        private Long authId;
    }
}
