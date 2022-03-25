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


    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserModifiedInfo {
        private String result;
        private String msg;
        private Long userId;
        private String username;
        private String userNickname;
        private String userRegion;
        private String userModifiedImg;
    }



}
