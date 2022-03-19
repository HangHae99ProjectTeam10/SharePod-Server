package com.spring.sharepod.dto.response.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserInfoResponseDto {
    private Long userid;
    private String username;
    private String nickname;
    private String mapdata;
    private String userimg;

}
