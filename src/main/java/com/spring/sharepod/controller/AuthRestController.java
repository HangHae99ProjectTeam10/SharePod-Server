package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.Auth.AuthBoolRequestDto;
import com.spring.sharepod.dto.request.Auth.AuthImgUploadRequestDto;
import com.spring.sharepod.model.AuthBool;
import com.spring.sharepod.model.AuthImg;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.service.AuthService;
import com.spring.sharepod.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class AuthRestController {
    private final S3Service s3Service;
//    private final AuthService authService;

//    @PostMapping("/auth/img/{userid}")
//    public ResponseEntity<AuthImg> AuthImgUpload(@RequestPart MultipartFile authfile, @PathVariable Long userid){
//
//
//
//        return new ResponseEntity<>(new AuthImg())
//    }

    private final AuthService authService;
    @PostMapping("/auth/img/bool")
    public ResponseEntity<Success> AuthBool(@RequestBody AuthBoolRequestDto authBoolRequestDto){
        authService.BoolAuth(authBoolRequestDto);
        return new ResponseEntity<>(new Success("success"," 사진 인증 성공"), HttpStatus.OK);
    }

}
