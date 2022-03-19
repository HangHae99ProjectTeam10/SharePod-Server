package com.spring.sharepod.exception.TokenError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode {


    WRONG_TOKEN(HttpStatus.BAD_REQUEST, "400_WRONG_TOKEN_1", "잘못된 토큰입니다."),
    UNKNOWN_ERROR(HttpStatus.BAD_REQUEST, "400_UNKNOWN_ERROR_1", "확인되지 않은 에러입니다."),
    WRONG_TYPE_TOKEN(HttpStatus.BAD_REQUEST, "400_WRONG_TYPE_TOKEN_1", "잘못된 타입의 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "400_EXPIRED_TOKEN_1", "엑세스 토큰 만료되었습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "400_UNSUPPORTED_TOKEN_1", "지원되지 않는 토큰입니다."),
    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "400_ACCESS_DENIED_1", "권한이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
}
