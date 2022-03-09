package com.spring.sharepod.controller;


import com.spring.sharepod.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class LikeRestController {
    private final LikeService likeService;

}
