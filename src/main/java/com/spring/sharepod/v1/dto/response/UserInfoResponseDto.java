package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserInfoResponseDto {
    private Long userId;
    private String username;
    private String nickName;
    private String userRegion;
    private String userImg;
    private LocalDateTime createdAt;
}

