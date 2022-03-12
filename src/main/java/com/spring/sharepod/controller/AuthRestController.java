package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.Auth.AuthBoolRequestDto;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.model.AuthImg;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.service.AuthService;
import com.spring.sharepod.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class AuthRestController {
    private final S3Service s3Service;
    private final AuthService authService;

    //buyer가 인증 이미지 저장
    @PostMapping("/auth/img/{userid}/{authimgboxid}")
    public BasicResponseDTO AuthImgUpload(@PathVariable Long userid, @PathVariable Long authimgboxid, @RequestPart MultipartFile authfile) throws IOException {

        //인증 사진 저장 및 유저 정보 맞는지 확인
        String s3authimgurl = s3Service.authimgboxs3(userid, authimgboxid, authfile);

        //authimgbox 저장 및 반환
        return authService.authimguploadService(authimgboxid,s3authimgurl);
    }

    //이미지 인증 창 데이터들
    @GetMapping("/auth/img/{authid}")
    public AuthDataAllResponseDTO authDataAllResponseDTO(@PathVariable Long authid){
        return authService.dataAllResponseDTO(authid);
    }

    private final AuthService authService;
    @PostMapping("/auth/img/bool")
    public ResponseEntity<Success> AuthBool(@RequestBody AuthBoolRequestDto authBoolRequestDto){
        authService.BoolAuth(authBoolRequestDto);
        return new ResponseEntity<>(new Success("success"," 사진 인증 성공"), HttpStatus.OK);
    }

}
