package com.spring.sharepod.exception.TokenError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CAuthenticationEntryPointException extends RuntimeException{
    private final TokenErrorCode tokenErrorCode;

    public CAuthenticationEntryPointException(String s, String message, TokenErrorCode errorCode) {
        this.tokenErrorCode = errorCode;
    }

}
