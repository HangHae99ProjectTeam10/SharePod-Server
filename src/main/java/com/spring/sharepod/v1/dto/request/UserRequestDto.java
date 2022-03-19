package com.spring.sharepod.v1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class UserRequestDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Register{
        private String userImg;
        private String username;
        private String nickName;
        private String password;
        private String passwordCheck;
        private String userRegion;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Modify{
        private String username;
        private String nickName;
        private String userRegion;
        private String userImg;
    }

    @Getter
    @NoArgsConstructor
    public static class Login {
        private String username;
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(username, password);
        }

    }

    @Getter
    @Setter
    public static class Reissue {
        private String accessToken;

        private String refreshToken;
    }

    @Getter
    @Setter
    public static class Logout {
        private String accessToken;

        private String refreshToken;
    }


    @Getter
    @Setter
    public static class UserDelete{
        private String username;
        private String password;
    }

}
