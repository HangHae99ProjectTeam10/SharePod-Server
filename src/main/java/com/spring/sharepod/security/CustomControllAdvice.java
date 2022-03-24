package com.spring.sharepod.security;

import com.spring.sharepod.exception.TokenError.TokenErrorCodeException;
import com.spring.sharepod.exception.TokenError.TokenErrorCodeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomControllAdvice extends ResponseEntityExceptionHandler {

    //JWT Filter에서 발생시키는 경우 ControllerAdvice에서 처리를 하지 못한다.
    //PermissionDeniedException(Custom Exception)
    @ExceptionHandler(TokenErrorCodeException.class)
    protected ResponseEntity<TokenErrorCodeResponse> handlePermissionDeniedException(TokenErrorCodeException e) {
        return TokenErrorCodeResponse.toResponseEntity(e.getTokenErrorCode());
    }
}
