package com.spring.sharepod.v1.repository.Auth;

import com.spring.sharepod.v1.dto.response.Auth.AuthDataResponseDto;

import java.util.List;


public interface AuthRepositoryCustom {

    Boolean authtest(Long authId);

    //20번 이미지 인증 창 데이터(인증 사진 확인하기)
    //auth 데이터 값 가져오기
    List<AuthDataResponseDto> getauthresponse(Long authId);
}
