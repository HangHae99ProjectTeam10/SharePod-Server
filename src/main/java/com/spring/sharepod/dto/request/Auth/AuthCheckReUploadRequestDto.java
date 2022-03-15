package com.spring.sharepod.dto.request.Auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthCheckReUploadRequestDto {
    private Long authid;
    private Long sellerid;
    private boolean authreupload;
}
