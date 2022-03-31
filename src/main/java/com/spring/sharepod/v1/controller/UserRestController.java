package com.spring.sharepod.v1.controller;

import com.spring.sharepod.entity.User;
import com.spring.sharepod.v1.dto.request.UserRequestDto;
import com.spring.sharepod.v1.dto.response.*;
import com.spring.sharepod.v1.dto.response.User.*;
import com.spring.sharepod.v1.service.AwsS3Service;
import com.spring.sharepod.v1.service.UserService;
import com.spring.sharepod.v1.validator.TokenValidator;
import com.spring.sharepod.v1.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
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

    //1번 API 로그인

    //유저에게 받아온 정보를 (UserRequestDto.Login)를 통하여 컨트롤러에서의 예외처리 후
    //(userService.login)으로 보낸 후  Refresh토큰과 Access토큰을 헤더에 담고, db에서 가져온 정보를 body에 담아
    //(UserResponseDto.Login)를 통해 컨트롤러로 보낸후 유저한테 보냄

    @PostMapping("/user/login")
    public UserResponseDto.Login Login(@RequestBody UserRequestDto.Login userLoginRequest, HttpServletResponse res) {
        //이메일이나 패스워드가 null 값일 경우의 처리
        userValidator.ValidLoginRequest(userLoginRequest);

        return userService.login(userLoginRequest, res);
    }

    //2번 API 토큰 재발급을 위한 api
    @PostMapping("/reissue")
    public BasicResponseDTO reissue(@RequestBody UserRequestDto.Reissue reissue, HttpServletResponse res, HttpServletRequest req) {
        return userService.reissue(reissue, res, req);
    }

    //3번 API 로그아웃
    @PostMapping("/user/logout")
    public BasicResponseDTO logout(@RequestBody UserRequestDto.Reissue reIssueRequestDto, HttpServletRequest req) {
        return userService.logout(reIssueRequestDto, req);
    }


    //4번 API 회원가입
    @PostMapping("/user/register")
    public BasicResponseDTO UserRegister(@RequestPart UserRequestDto.Register userRegisterRequestDto,
                                                @RequestPart MultipartFile imgFile) throws IOException {
        //유저 프로필 업로드
        String userimg = awsS3Service.upload(userRegisterRequestDto, imgFile);
        userRegisterRequestDto.setUserImg(userimg);

        //회원가입 완료
        return userService.registerUser(userRegisterRequestDto);
    }

    //5번 API 마이페이지 불러오기
    @GetMapping("/user/{userId}")
    public UserMyInfoResponseDto getBoardList(@PathVariable Long userId, @AuthenticationPrincipal User user) {

        //토큰과 userid 일치 확인
        tokenValidator.userIdCompareToken(userId, user.getId());

        return userService.getUserInfo(userId);
    }

    @GetMapping("/user/like/{userId}")
    public UserResponseDto.UserLikedList userLikedList(@PathVariable Long userId,@AuthenticationPrincipal User user){
        tokenValidator.userIdCompareToken(userId,user.getId());
        return userService.getUserLikeBoard(userId);
    }
    @GetMapping("/user/board/{userId}")
    public UserResponseDto.UserMyBoardList userMyBoardList(@PathVariable Long userId,@AuthenticationPrincipal User user){
        tokenValidator.userIdCompareToken(userId,user.getId());
        return userService.getMyBoard(userId);
    }
    @GetMapping("/user/order/{userId}")
    public UserOrderResponseDto userBuyerList(@PathVariable Long userId, @AuthenticationPrincipal User user){
        tokenValidator.userIdCompareToken(userId,user.getId());
        List<RentBuyer> rentBuyerList = userService.getBuyList(userId);
        List<RentSeller> rentSellerList = userService.getSellList(userId);
        List<UserReservation> userReservationList = userService.getReservationList(userId);
        return UserOrderResponseDto.builder()
                .result("success")
                .msg("내 정보 조회 성공")
                .rentBuyerList(rentBuyerList)
                .rentSellerList(rentSellerList)
                .userReservationList(userReservationList)
                .build();
    }

    //6번 회원 정보 수정하기
    @PatchMapping("/user/{userId}")
    public UserResponseDto.UserModifiedInfo UserModify(@PathVariable Long userId,
                                       @RequestPart UserRequestDto.Modify userModifyRequestDTO,
                                       @RequestPart(required=false) MultipartFile userImgFile, @AuthenticationPrincipal User user) throws IOException {
        //토큰과 userid 일치 확인
        tokenValidator.userIdCompareToken(userId, user.getId());

        //해당 request vaildator 작동
        userValidator.ValidModifiedUser(userModifyRequestDTO);


        //이미지가 새롭게 들어왔으면
        if (!Objects.equals(null, StringUtils.getFilenameExtension(userImgFile.getOriginalFilename()))){
            //변경된 사진 저장 후 기존 삭제 삭제 후 requestDto에 setUserimg 하기
            userModifyRequestDTO.setUserImg(awsS3Service.ModifiedProfileImg(user.getUserImg().substring(user.getUserImg().lastIndexOf("/") + 1), user.getNickName(), userImgFile));
        } else {
            userModifyRequestDTO.setUserImg(user.getUserImg());
        }

        return userService.usermodifyService(userId, userModifyRequestDTO);
    }

    //7번 API 회원 탈퇴하기
    @DeleteMapping("/user/{userId}")
    public BasicResponseDTO DeleteUser(@PathVariable Long userId, @RequestBody UserRequestDto.Login userDelete, @AuthenticationPrincipal User user) {
        //토큰과 userid 일치 확인
        tokenValidator.userIdCompareToken(userId, user.getId());
        return userService.UserDelete(userId, userDelete);
    }


}