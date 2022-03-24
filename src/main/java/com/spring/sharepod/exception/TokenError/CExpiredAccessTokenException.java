package com.spring.sharepod.exception.TokenError;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CExpiredAccessTokenException extends RuntimeException {
    private final TokenErrorCode tokenErrorCode;

    public CExpiredAccessTokenException(String s, String message, TokenErrorCode errorCode) {
        this.tokenErrorCode = errorCode;

    }
}