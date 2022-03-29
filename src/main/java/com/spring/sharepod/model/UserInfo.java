package com.spring.sharepod.model;

import com.spring.sharepod.v1.dto.response.User.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {
    private String result;
    private String msg;
    private UserInfoResponseDto userInfo;

}