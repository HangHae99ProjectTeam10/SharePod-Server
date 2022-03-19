package com.spring.sharepod.dto.response.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginReturnResponseDTO {
    private String result;
    private String msg;
    private Long userid;
    private String nickname;
    private String mapdata;
    private String userimg;
}
