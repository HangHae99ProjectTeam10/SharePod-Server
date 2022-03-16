package com.spring.sharepod.exception.TokenError;


import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class TokenErrorCodeResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String errorCode;
    private final String msg;

    public static ResponseEntity<TokenErrorCodeResponse> toResponseEntity(TokenErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(TokenErrorCodeResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .errorCode(errorCode.getErrorCode())
                        .msg(errorCode.getErrorMessage())
                        .build()
                );
    }
}
