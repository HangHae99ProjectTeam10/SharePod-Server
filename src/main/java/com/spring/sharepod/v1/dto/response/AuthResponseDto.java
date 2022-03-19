package com.spring.sharepod.v1.dto.response;

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
        private List<AuthResponseDto.AuthData> data;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthData {
        private Long authImgId;
        private String authImgUrl;
        private boolean authImgCheck;
    }
}
