package com.spring.sharepod.v1.service;

import com.spring.sharepod.entity.AuthImg;
import com.spring.sharepod.v1.dto.request.AuthRequestDto;
import com.spring.sharepod.v1.dto.response.Auth.AuthResponseDto;
import com.spring.sharepod.v1.validator.AuthImgValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthImgService {
    private final AuthImgValidator authImgValidator;

    //21번 buyer가 인증 사진 업로드
    @javax.transaction.Transactional
    public AuthResponseDto.AuthUploadDTO authimguploadService(Long userId, Long authImgId, String imgUrl) {

        //authImgId로 찾아서 그냥 넣는다.
        AuthImg authImg = authImgValidator.ValidAuthImgById(authImgId);

        //이미지 DB 넣어주기
        authImg.updateImgUrl(imgUrl);
        return AuthResponseDto.AuthUploadDTO.builder()
                .result("success")
                .msg("사진 등록성공")
                .userId(userId)
                .authImgId(authImgId)
                .authImgUrl(imgUrl)
                .build();
    }

    //22번 API 빌려준 사람만의 기능, 인증 성공 or 실패
    @Transactional
    public AuthResponseDto.AuthImgBoolDTO BoolAuth(AuthRequestDto.AuthImgCheck authBoolRequestDto) {
        Long sellerId = authBoolRequestDto.getSellerId();

        //주어진 id에 대해서 imgbox가 존재하는지 확인
        AuthImg authimg = authImgValidator.ValidAuthImgById(authBoolRequestDto.getAuthImgId());

        //해당 authimgbox에서 받아온 seller의 id와 보내준 seller의 id가 일치하는지 확인
        authImgValidator.ValidAuthImgBoxIdEqualSellerId(authimg.getAuth().getAuthSeller().getId(), sellerId);

        //둘 다 통과가 되면 true, false를 업데이트 해줌
        authimg.AuthBoolupdate(authBoolRequestDto.getCheck());
        return AuthResponseDto.AuthImgBoolDTO.builder()
                .result("success")
                .msg("사진 인증 등록성공")
                .authImgId(authBoolRequestDto.getAuthImgId())
                .sellerid(authBoolRequestDto.getSellerId())
                .check(authBoolRequestDto.getCheck())
                .build();
    }
}
