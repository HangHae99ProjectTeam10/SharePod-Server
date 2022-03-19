package com.spring.sharepod.v1.controller;

import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.v1.dto.request.AuthRequestDto;
import com.spring.sharepod.v1.dto.response.AuthResponseDto;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.service.AuthImgService;
import com.spring.sharepod.v1.service.AuthService;
import com.spring.sharepod.v1.service.AwsS3Service;
import com.spring.sharepod.v1.validator.AuthValidator;
import com.spring.sharepod.v1.validator.TokenValidator;
import com.spring.sharepod.v1.validator.UserValidator;
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
    private final AuthService authService;
    private final UserValidator userValidator;
    private final TokenValidator tokenValidator;
    private final AuthImgService authImgService;
    private final AuthValidator authValidator;
    private final AwsS3Service awsS3Service;

    //20번 이미지 인증 창 데이터
    @GetMapping("/auth/img/{authId}")
    public AuthResponseDto.AuthDataAll authDataAllResponseDTO(@PathVariable Long authId, @AuthenticationPrincipal User user){
        //토큰이 존재하는지에 대한 것만 확인해야함
        if(user==null){
            throw new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND);
        }

        return authService.dataAllResponseDTO(authId);
    }

    //21번 buyer가 인증 이미지 저장 (구현 완료)
    @PostMapping("/auth/img/{userId}/{authImgId}")
    public BasicResponseDTO AuthImgUpload(@PathVariable Long userId, @PathVariable Long authImgId, @RequestPart MultipartFile authFile, @AuthenticationPrincipal User user) throws IOException {
        // 토큰과 userid 일치하는지 확인
        tokenValidator.userIdCompareToken(userId,user.getId());

        //인증 사진 저장 및 유저 정보 맞는지 확인
        String s3authImgUrl = awsS3Service.authImgCheck(userId, authImgId, authFile);

        //authImg 저장 및 반환
        return authImgService.authimguploadService(authImgId,s3authImgUrl);
    }


    //23번 재업로드 or 삭제 api (구현 완료)
    @PostMapping("/auth/reupload")
    public ResponseEntity<Success> AuthBool(@RequestBody AuthRequestDto.AuthCheckReUpload authCheckReUploadRequestDto, @AuthenticationPrincipal User user){
        //토큰과 authBoolRequestDto.getSellerid()가 일치하는지에 대한 판단
        tokenValidator.userIdCompareToken(authCheckReUploadRequestDto.getSellerId(), user.getId());

        //seller id가 user 테이블에 존재하는지에 대한 판단
        userValidator.ValidByUserId(authCheckReUploadRequestDto.getSellerId());

        //service에서 requestDto로 비즈니스 로직 구현
        Long id = authService.CheckReuploadBoard(authCheckReUploadRequestDto);

        //reupload에 따라서 메시지 다르게 호출
        String result = authValidator.ValidAuthByReupload(authCheckReUploadRequestDto.isAuthReUpload());

        return new ResponseEntity<>(new Success("success",id +"번 "+ result), HttpStatus.OK);
    }

}
