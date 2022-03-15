package com.spring.sharepod.dto.request.Auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthBoolRequestDto {
    private Long imgboxcheckid;
    private Long sellerid;
    private Boolean check;
}
