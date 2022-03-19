package com.spring.sharepod.v1.controller;


import com.spring.sharepod.entity.User;
import com.spring.sharepod.v1.dto.request.LikeRequestDTO;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.service.LikedService;
import com.spring.sharepod.v1.validator.TokenValidator;
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
    @PostMapping("/like/{boardId}")
    public BasicResponseDTO likecontroll(@PathVariable Long boardId, @RequestBody LikeRequestDTO.Liked requestDTO, @AuthenticationPrincipal User user){
        System.out.println("user.getId()"+user.getId());
        tokenValidator.userIdCompareToken(requestDTO.getUserId(),user.getId());
        return likedService.islikeservice(boardId,requestDTO);
    }
}
