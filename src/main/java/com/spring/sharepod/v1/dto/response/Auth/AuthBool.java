package com.spring.sharepod.v1.dto.response.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthBool {
    private String result;
    private String msg;
}
