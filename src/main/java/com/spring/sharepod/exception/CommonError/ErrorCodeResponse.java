package com.spring.sharepod.exception.CommonError;


import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorCodeResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String errorCode;
    private final String msg;

    public static ResponseEntity<ErrorCodeResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorCodeResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .errorCode(errorCode.getErrorCode())
                        .msg(errorCode.getErrorMessage())
                        .build()
                );
    }


}