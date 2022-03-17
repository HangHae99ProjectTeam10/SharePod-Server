package com.spring.sharepod.controller;
import com.spring.sharepod.dto.request.Auth.AuthCheckReUploadRequestDto;
import com.spring.sharepod.dto.response.Auth.AuthDataAllResponseDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.service.AuthImgService;
import com.spring.sharepod.service.AuthService;
import com.spring.sharepod.service.AwsS3Service;
import com.spring.sharepod.service.S3Service;
import com.spring.sharepod.validator.AuthValidator;
import com.spring.sharepod.validator.TokenValidator;
import com.spring.sharepod.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class AuthRestController {
    private final AwsS3Service awsS3Service;
    private final AuthService authService;
    private final UserValidator userValidator;
    private final TokenValidator tokenValidator;
    private final AuthImgService authImgService;
    private final AuthValidator authValidator;
    private final S3Service s3Service;

    //20번 이미지 인증 창 데이터
    @GetMapping("/auth/img/{authid}")
    public AuthDataAllResponseDTO authDataAllResponseDTO(@PathVariable Long authid, @AuthenticationPrincipal User user){
        //토큰이 존재하는지에 대한 것만 확인해야함
        if(user==null){
            throw new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND);
        }

        return authService.dataAllResponseDTO(authid);
    }

    //21번 buyer가 인증 이미지 저장
    @PostMapping("/auth/img/{userid}/{authimgboxid}")
    public BasicResponseDTO AuthImgUpload(@PathVariable Long userid, @PathVariable Long authimgboxid, @RequestPart MultipartFile authfile, @AuthenticationPrincipal User user) throws IOException {
        // 토큰과 userid 일치하는지 확인
        tokenValidator.userIdCompareToken(userid,user.getId());

        //인증 사진 저장 및 유저 정보 맞는지 확인
        String s3authimgurl = s3Service.authimgboxs3(userid, authimgboxid, authfile);

        //authimgbox 저장 및 반환
        return authImgService.authimguploadService(userid,authimgboxid,s3authimgurl,user);
    }


    //23번 재업로드 or 삭제 api
    @PostMapping("/auth/reupload")
    public ResponseEntity<Success> AuthBool(@RequestBody AuthCheckReUploadRequestDto authCheckReUploadRequestDto, @AuthenticationPrincipal User user){
        //토큰과 authBoolRequestDto.getSellerid()가 일치하는지에 대한 판단
        tokenValidator.userIdCompareToken(authCheckReUploadRequestDto.getSellerid(), user.getId());

        //seller id가 user 테이블에 존재하는지에 대한 판단
        userValidator.ValidByUserId(authCheckReUploadRequestDto.getSellerid());

        //service에서 requestDto로 비즈니스 로직 구현
        Long id = authService.CheckReuploadBoard(authCheckReUploadRequestDto);

        //reupload에 따라서 메시지 다르게 호출
        String result = authValidator.ValidAuthByReupload(authCheckReUploadRequestDto.isAuthreupload());


        return new ResponseEntity<>(new Success("success",id +"번 "+ result), HttpStatus.OK);
    }

}
