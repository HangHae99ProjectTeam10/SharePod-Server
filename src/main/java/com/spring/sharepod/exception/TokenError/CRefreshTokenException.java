package com.spring.sharepod.exception.TokenError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CRefreshTokenException extends RuntimeException {
    private final TokenErrorCode tokenErrorCode;

    public CRefreshTokenException(String s, String message, TokenErrorCode errorCode) {
        this.tokenErrorCode = errorCode;
    }
}

