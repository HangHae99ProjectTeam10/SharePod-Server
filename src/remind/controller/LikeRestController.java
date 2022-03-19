package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.Liked.LikeRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.service.LikedService;
import com.spring.sharepod.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class LikeRestController {
    private final LikedService likedService;
    private final TokenValidator tokenValidator;

    //찜하기, 찜하기 취소
    @PostMapping("/like/{boardid}")
    public BasicResponseDTO likecontroll(@PathVariable Long boardid, @RequestBody LikeRequestDTO requestDTO, @AuthenticationPrincipal User user){
        System.out.println("user.getId()"+user.getId());
        tokenValidator.userIdCompareToken(requestDTO.getUserid(),user.getId());
        return likedService.islikeservice(boardid,requestDTO);
    }
}
