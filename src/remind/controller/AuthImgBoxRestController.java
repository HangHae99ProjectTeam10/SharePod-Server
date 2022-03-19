package com.spring.sharepod.controller;

import com.spring.sharepod.dto.request.Auth.AuthBoolRequestDto;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.service.AuthImgService;
import com.spring.sharepod.validator.TokenValidator;
import com.spring.sharepod.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthImgBoxRestController {
    private final TokenValidator tokenValidator;
    private final UserValidator userValidator;
    private final AuthImgService authImgService;

    //22번 빌려준 사람의 인증 성공 or 실패
    @PostMapping("/auth/img/bool")
    public ResponseEntity<Success> AuthBool(@RequestBody AuthBoolRequestDto authBoolRequestDto, @AuthenticationPrincipal User user){
        //토큰과 authBoolRequestDto.getSellerid()가 일치하는지에 대한 판단
        tokenValidator.userIdCompareToken(authBoolRequestDto.getSellerid(), user.getId());

        //seller id가 user 테이블에 존재하는지에 대한 판단
        userValidator.ValidByUserId(authBoolRequestDto.getSellerid());


        authImgService.BoolAuth(authBoolRequestDto);
        return new ResponseEntity<>(new Success("success"," 사진 인증 성공"), HttpStatus.OK);
    }
}