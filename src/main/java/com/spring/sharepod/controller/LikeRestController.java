package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.LikeRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class LikeRestController {
    private final LikedService likedService;

    //찜하기, 찜하기 취소
    @PostMapping("/like/{boardid}")
    public BasicResponseDTO likecontroll(@PathVariable Long boardid, @RequestBody LikeRequestDTO requestDTO){
        return likedService.islikeservice(boardid,requestDTO);
    }

}
