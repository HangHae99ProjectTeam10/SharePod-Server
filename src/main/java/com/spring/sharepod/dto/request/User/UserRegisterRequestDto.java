package com.spring.sharepod.dto.request.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegisterRequestDto {
//    {
//  “userimg : “이미지 파일”,
//  “username” :  “이메일 형식”,
//  “nickname” : “닉네임”,
//  “password” : “비밀번호”,
//  “passwordcheck” : “비밀번호확인”,
//  “mapdata” : “지역”
//    }

    private String userimg;
    private String username;
    private String nickname;
    private String password;
    private String passwordcheck;
    private String mapdata;

}
