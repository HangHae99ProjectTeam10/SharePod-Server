package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    // 유저 생성하기 (JSON)
    @PostMapping("/user/register")
    public ResponseEntity<Success> createUser(@RequestBody UserRegisterRequestDto userRegisterRequestDto){
        Long userId = userService.createUser(userRegisterRequestDto);
        return new ResponseEntity<>(new Success("success", "회원 가입 성공하였습니다."), HttpStatus.OK);
    }
}
