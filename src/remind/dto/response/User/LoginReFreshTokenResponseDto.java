package com.spring.sharepod.dto.response.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Builder
@Getter
@AllArgsConstructor
public class LoginReFreshTokenResponseDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
}
