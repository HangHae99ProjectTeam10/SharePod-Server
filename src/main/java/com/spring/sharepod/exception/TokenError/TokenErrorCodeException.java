package com.spring.sharepod.exception.TokenError;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenErrorCodeException extends RuntimeException{
    private final TokenErrorCode tokenErrorCode;

    public TokenErrorCodeException(String s, String message, TokenErrorCode errorCode){
        this.tokenErrorCode = errorCode;
    }
}
