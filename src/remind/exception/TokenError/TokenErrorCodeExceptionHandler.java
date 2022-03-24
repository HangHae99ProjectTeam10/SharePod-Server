package com.spring.sharepod.exception.TokenError;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


@Slf4j // 콘솔에 에러 로그 찍기!
@RestControllerAdvice
public class TokenErrorCodeExceptionHandler {
    /**
     * -1003
     * 전달한 Jwt 이 정상적이지 않은 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<TokenErrorCodeResponse> authenticationEntrypointException(TokenErrorCodeException e) {
        log.error("Error - ErrorCodeException : " + e.getTokenErrorCode());
        return TokenErrorCodeResponse.toResponseEntity(e.getTokenErrorCode());
    }

    /**
     * -1004
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     */
    @ExceptionHandler(CAccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<TokenErrorCodeResponse> accessDeniedException(TokenErrorCodeException e) {
        log.error("Error - ErrorCodeException : " + e.getTokenErrorCode());
        return TokenErrorCodeResponse.toResponseEntity(e.getTokenErrorCode());
    }

//
    /**
     * -1005
     * refresh token 에러시 발생 시키는 에러
     */
    @ExceptionHandler(CRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<TokenErrorCodeResponse> CRefreshTokenException(TokenErrorCodeException e) {
        log.error("Error - ErrorCodeException : " + e.getTokenErrorCode());
        return TokenErrorCodeResponse.toResponseEntity(e.getTokenErrorCode());
    }
//
    /**
     * -1006
     * 액세스 토큰 만료시 발생하는 에러
     */
    @ExceptionHandler(CExpiredAccessTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<TokenErrorCodeResponse> CExpiredAccessTokenException(TokenErrorCodeException e) {
        log.error("Error - ErrorCodeException : " + e.getTokenErrorCode());
        return TokenErrorCodeResponse.toResponseEntity(e.getTokenErrorCode());
    }
}
