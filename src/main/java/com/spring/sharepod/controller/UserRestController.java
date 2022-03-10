package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.service.S3Service;
import com.spring.sharepod.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class UserRestController {
    private final UserService userService;
    private final S3Service s3Service;
    //private final FileUploadService fileUploadService;

    // 유저 생성하기 (JSON)
    @PostMapping("/user/register")
    public ResponseEntity<Success> createUser(@RequestPart UserRegisterRequestDto userRegisterRequestDto,
                                              @RequestPart MultipartFile imgfile) throws IOException {

        //유저 프로필 업로드
        String userimg = s3Service.upload(userRegisterRequestDto, imgfile);
        userRegisterRequestDto.setUserimg(userimg);

        //회원가입 완료
        Long userId = userService.createUser(userRegisterRequestDto);
        return new ResponseEntity<>(new Success("success", "회원 가입 성공하였습니다."), HttpStatus.OK);
    }
}