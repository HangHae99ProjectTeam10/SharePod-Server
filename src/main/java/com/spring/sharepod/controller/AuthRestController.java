package com.spring.sharepod.controller;


import com.spring.sharepod.model.AuthImg;
import com.spring.sharepod.service.AuthService;
import com.spring.sharepod.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class AuthRestController {
    private final S3Service s3Service;
    private final AuthService authService;

    @PostMapping("/auth/img")
//    public ResponseEntity<AuthImg> AuthImgUpload(@RequestPart )

}
