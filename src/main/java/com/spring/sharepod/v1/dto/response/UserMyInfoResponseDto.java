package com.spring.sharepod.v1.dto.response;

import com.spring.sharepod.v1.dto.response.User.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserMyInfoResponseDto {
    private String result;
    private String msg;
    private UserInfoResponseDto userInfo;

}
