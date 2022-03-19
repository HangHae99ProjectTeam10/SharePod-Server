package com.spring.sharepod.v1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequestDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AuthCheckReUpload {
        private Long authId;
        private Long sellerId;
        private boolean authReUpload;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AuthImgCheck {
        private Long authImgId;
        private Long sellerId;
        private Boolean check;
    }

}
