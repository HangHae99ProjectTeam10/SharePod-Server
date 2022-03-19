package com.spring.sharepod.v1.controller;

import com.spring.sharepod.entity.User;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.v1.dto.request.AuthRequestDto;
import com.spring.sharepod.v1.service.AuthImgService;
import com.spring.sharepod.v1.validator.TokenValidator;
import com.spring.sharepod.v1.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthImgRestController {
    private final TokenValidator tokenValidator;
    private final UserValidator userValidator;
    private final AuthImgService authImgService;

    //22번 빌려준 사람의 인증 성공 or 실패 (구현 성공)
    @PostMapping("/auth/img/bool")
    public ResponseEntity<Success> AuthBool(@RequestBody AuthRequestDto.AuthImgCheck authBoolRequestDto, @AuthenticationPrincipal User user){
        //토큰과 authBoolRequestDto.getSellerid()가 일치하는지에 대한 판단
        tokenValidator.userIdCompareToken(authBoolRequestDto.getSellerId(), user.getId());

        //seller id가 user 테이블에 존재하는지에 대한 판단
        userValidator.ValidByUserId(authBoolRequestDto.getSellerId());


        authImgService.BoolAuth(authBoolRequestDto);
        return new ResponseEntity<>(new Success("success","사진 인증 성공"), HttpStatus.OK);
    }
}