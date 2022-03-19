package com.spring.sharepod.validator;

import com.spring.sharepod.dto.request.Auth.AuthCheckReUploadRequestDto;
import com.spring.sharepod.entity.Auth;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.spring.sharepod.exception.CommonError.ErrorCode.AUTH_NOT_SELLER;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class AuthValidator {

    private final AuthRepository authRepository;

    //authid로 auth가 없다면 error 메시지 호출
    public Auth ValidAuthByAuthId(Long authid) {
        Auth auth = authRepository.findById(authid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.AUTH_NOT_EXIST));
        return auth;
    }

    //reupload에 따라서 메시지 다르게 호출
    public String ValidAuthByReupload(Boolean reupload){
        String result = "";
        if (reupload){
            return result = "게시글 재 업로드 성공";
        }else{
            return result = "게시글 삭제 성공";
        }
    }

    public void ValidAuthBySellerIdEqualRequestId(AuthCheckReUploadRequestDto authCheckReUploadRequestDto, Long authsellerid){
        if(!Objects.equals(authCheckReUploadRequestDto.getSellerid(), authsellerid)){
            throw new ErrorCodeException(AUTH_NOT_SELLER);
        }
    }
}
