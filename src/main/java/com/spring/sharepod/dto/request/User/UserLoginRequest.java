package com.spring.sharepod.dto.request.User;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {
    private String username;
    private String password;
}
