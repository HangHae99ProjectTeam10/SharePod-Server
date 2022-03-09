package com.spring.sharepod.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorCodeException extends RuntimeException{
    private final ErrorCode errorCode;
}
