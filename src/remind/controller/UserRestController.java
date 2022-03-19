package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.User.ReIssueRequestDto;
import com.spring.sharepod.dto.request.User.UserLoginRequest;
import com.spring.sharepod.dto.request.User.UserModifyRequestDTO;
import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.dto.response.Board.RentBuyerResponseDto;
import com.spring.sharepod.dto.response.Board.RentSellerResponseDto;
import com.spring.sharepod.dto.response.Liked.LikedResponseDto;
import com.spring.sharepod.dto.response.User.LoginReturnResponseDTO;
import com.spring.sharepod.dto.response.User.UserInfoResponseDto;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.model.LogOut;
import com.spring.sharepod.model.ReFreshToken;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.model.UserInfo;
import com.spring.sharepod.service.AwsS3Service;
import com.spring.sharepod.service.S3Service;
import com.spring.sharepod.service.UserService;
import com.spring.sharepod.validator.TokenValidator;
import com.spring.sharepod.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class UserRestController {
    private final UserService userService;
    private final TokenValidator tokenValidator;
    private final UserValidator userValidator;
    private final AwsS3Service awsS3Service;
    private final S3Service s3Service;
    //private final FileUploadService fileUploadService;

    //로그인 구현하기
    @PostMapping("/user/login")
    public LoginReturnResponseDTO loginControll(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse res){
        return userService.loginReturnDTO(userLoginRequest,res);
    }
    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<ReFreshToken> reissue(@RequestBody ReIssueRequestDto reissue,HttpServletResponse res, HttpServletRequest req) {
        // validation check
//        if (errors.hasErrors()) {
//            return response.invalidFields(Helper.refineErrors(errors));
//        }
//        System.out.println(user.getId());
        return userService.reissue(reissue,res,req);
    }

    //로그 아웃
    @PostMapping("/user/logout")
    public ResponseEntity<LogOut> logout(@RequestBody ReIssueRequestDto reIssueRequestDto,HttpServletRequest req) {
        System.out.println(reIssueRequestDto.getAccessToken()+ "reIssueRequestDto.getAccessToken()");
        return userService.logout(reIssueRequestDto,req);
    }


    // 유저 생성하기 (JSON)
    @PostMapping("/user/register")
    public ResponseEntity<Success> createUser(@RequestPart UserRegisterRequestDto userRegisterRequestDto,
                                              @RequestPart MultipartFile imgfile) throws IOException {

        //이미지를 업로드 하기 전에 validator로 걸러줘야지 이미지가 안 들어간다.
        userValidator.validateUserRegisterData(userRegisterRequestDto);

        //유저 프로필 업로드
        String userimg = s3Service.upload(userRegisterRequestDto, imgfile);

        userRegisterRequestDto.setUserimg(userimg);

        //회원가입 완료
        Long userId = userService.createUser(userRegisterRequestDto);
        return new ResponseEntity<>(new Success("success", "회원 가입 성공하였습니다."), HttpStatus.OK);
    }

    //회원 정보 수정하기
    @PatchMapping("/user/{userid}")
    public BasicResponseDTO usermodify(@PathVariable Long userid,
                                       @RequestPart UserModifyRequestDTO userModifyRequestDTO,
                                       @RequestPart MultipartFile userimgfile, @AuthenticationPrincipal User user) throws IOException {
        //토큰과 userid 일치 확인
        tokenValidator.userIdCompareToken(userid, user.getId());
        System.out.println("modified userid =====  "  + user.getId());

        //해당 request vaildator 작동
        userValidator.validateUserChange(userModifyRequestDTO);

        //이미지가 새롭게 들어왔으면
        if(!Objects.equals(userimgfile.getOriginalFilename(), "")){
            //변경된 사진 저장 후 기존 삭제 삭제 후 requestDto에 setUserimg 하기
            userModifyRequestDTO.setUserimg(awsS3Service.ModifiedProfileImg(user.getUserimg().substring(user.getUserimg().lastIndexOf("/")+1), user.getNickname(), userimgfile));
        }else {
            userModifyRequestDTO.setUserimg(user.getUserimg());
        }

        return userService.usermodifyService(userid, userModifyRequestDTO);
    }

    //회원 탈퇴하기
    @DeleteMapping("/user/{userid}")
    public ResponseEntity<Success> DeleteUser(@PathVariable Long userid, @RequestBody UserLoginRequest userLoginRequest, @AuthenticationPrincipal User user){
        //토큰과 userid 일치 확인
        tokenValidator.userIdCompareToken(userid,user.getId());

        String nickname = userService.UserDelete(userid, userLoginRequest);
        return new ResponseEntity<>(new Success("success", nickname + " 님의 회원탈퇴 성공했습니다."),HttpStatus.OK);
    }

    //마이페이지 불러오기
    @GetMapping("/user/{userid}")
    public ResponseEntity<UserInfo> getBoardList(@PathVariable Long userid, @AuthenticationPrincipal User user) {

        //토큰과 userid 일치 확인
        tokenValidator.userIdCompareToken(userid,user.getId());

        //각각의 데이터 받아오기
        UserInfoResponseDto userinfo = userService.getUserInfo(userid);
        List<LikedResponseDto> userlikeboard = userService.getUserLikeBoard(userid);
        List<MyBoardResponseDto> usermyboard = userService.getMyBoard(userid);
        List<RentBuyerResponseDto> rentbuyer = userService.getBuyList(userid);
        List<RentSellerResponseDto> rentseller = userService.getSellList(userid);
        return new ResponseEntity<>(new UserInfo("success", "내 정보 불러오기 성공", userinfo,userlikeboard,usermyboard,rentbuyer,rentseller), HttpStatus.OK);
    }
}