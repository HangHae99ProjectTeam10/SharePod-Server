package com.spring.sharepod.v1.repository.Auth;

import com.spring.sharepod.v1.dto.response.Auth.AuthDataResponseDto;

import java.util.List;


public interface AuthRepositoryCustom {

    Boolean authtest(Long authId);

    List<AuthDataResponseDto> getauthresponse(Long authId);
}
