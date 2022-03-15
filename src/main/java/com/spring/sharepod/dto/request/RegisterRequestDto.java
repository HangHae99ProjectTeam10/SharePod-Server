package com.spring.sharepod.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter

@NoArgsConstructor
//public class SignupRequestDto {
//
//    @NotBlank(message = "아이디를 입력해주세요")
//    @Pattern(regexp="^[a-zA-Z0-9]{3,12}$", message="아이디를 3~12자로 입력해주세요.(특수문자x)")
//    private String userId;
//
//    @Pattern(regexp="^[a-zA-Z0-9]{4,12}$", message="비밀번호를 4~12자로 입력해주세요.")
//    private String password;
//
//    @NotBlank(message = "확인 비밀번호를 입력해주세요")
//    private String checkPw;
//    @AssertTrue(message = "입력한 비밀번호와 같지 않습니다") public boolean isSamePwd() { return password.equals(checkPw); }
//
//
//    @NotBlank(message = "닉네임을 입력해주세요")
//    private String nickname;
//
//    private String imgUrl;
//
public class RegisterRequestDto {
    private String username;
    private String password;
    private String passwordcheck;
    private String nickname;
    private String userimg;
    private String mapdata;
}

