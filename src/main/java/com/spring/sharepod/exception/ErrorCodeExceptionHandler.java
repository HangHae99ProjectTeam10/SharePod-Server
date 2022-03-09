package com.spring.sharepod.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j // 콘솔에 에러 로그 찍기!
@RestControllerAdvice
public class ErrorCodeExceptionHandler extends ResponseEntityExceptionHandler {

    // ErrorCode 에서 만든 커스텀 에러들을 보낼 수 있다! 이거 너무좋다.
    @ExceptionHandler( value = { ErrorCodeException.class })
    protected ResponseEntity<ErrorCodeResponse> handleCustomException(ErrorCodeException e) {
        log.error("Error - ErrorCodeException : " + e.getErrorCode());
        return ErrorCodeResponse.toResponseEntity(e.getErrorCode());
    }
}
