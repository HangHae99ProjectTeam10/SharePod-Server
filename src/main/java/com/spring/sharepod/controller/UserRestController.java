package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.User.UserModifyRequestDTO;
import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.dto.response.Board.RentBuyerResponseDto;
import com.spring.sharepod.dto.response.Board.RentSellerResponseDto;
import com.spring.sharepod.dto.response.Liked.LikedResponseDto;
import com.spring.sharepod.dto.response.UserInfoResponseDto;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.model.UserInfo;
import com.spring.sharepod.service.S3Service;
import com.spring.sharepod.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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


    //마이페이지 불러오기
    @GetMapping("/user/{userid}")
    public ResponseEntity<UserInfo> getBoardList(@PathVariable Long userid) {
        UserInfoResponseDto userinfo = userService.getUserInfo(userid);
        List<LikedResponseDto> userlikeboard = userService.getUserLikeBoard(userid);
        List<MyBoardResponseDto> usermyboard = userService.getMyBoard(userid);
        List<RentBuyerResponseDto> rentbuyer = userService.getBuyList(userid);
        List<RentSellerResponseDto> rentseller = userService.getSellList(userid);
        return new ResponseEntity<>(new UserInfo("success", "내 정보 불러오기 성공", userinfo,userlikeboard,usermyboard,rentbuyer,rentseller), HttpStatus.OK);
    }

    //회원 정보 수정하기
    @PatchMapping("/user/{userid}")
    public BasicResponseDTO usermodify(@PathVariable Long userid,
                                       @RequestPart UserModifyRequestDTO userModifyRequestDTO,
                                       @RequestPart MultipartFile userimgfile) throws IOException {

        //이미지가 새롭게 들어왔으면
        if(!Objects.equals(userimgfile.getOriginalFilename(), "")){
            //변경된 사진 저장 후 이름 넘겨주기
            userModifyRequestDTO.setUserimg(s3Service.userprofileimgchange(userimgfile));
        }

        return userService.usermodifyService(userid, userModifyRequestDTO);
    }

}