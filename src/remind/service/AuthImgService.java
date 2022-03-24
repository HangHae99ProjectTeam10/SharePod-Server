package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.Auth.AuthBoolRequestDto;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.entity.Authimgbox;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.validator.AuthimgboxValidator;
import com.spring.sharepod.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthImgService {
    private final TokenValidator tokenValidator;
    private final AuthimgboxValidator authimgboxValidator;

    @javax.transaction.Transactional
    public BasicResponseDTO authimguploadService(Long userid, Long authimgboxid, String imgurl, @AuthenticationPrincipal User user) {
        //user가 토큰과 일치하는지
        tokenValidator.userIdCompareToken(userid,user.getId());

        //구매자가 id가 일치하는지를 확인한다.
        Authimgbox authimgbox = authimgboxValidator.ValidAuthImgBoxByBuyerId(userid);

        //이미지 DB 넣어주기
        authimgbox.update(imgurl);

        return BasicResponseDTO.builder()
                .result("success")
                .msg("사진 등록성공")
                .build();
    }


    //빌려준 사람만의 기능, 인증 성공 or 실패
    @Transactional
    public void BoolAuth(AuthBoolRequestDto authBoolRequestDto) {
        Long sellerId = authBoolRequestDto.getSellerid();

        //주어진 id에 대해서 imgbox가 존재하는지 확인
        Authimgbox authimgbox = authimgboxValidator.ValidAuthImgBoxById(authBoolRequestDto.getImgboxcheckid());

        //해당 authimgbox에서 받아온 seller의 id와 보내준 seller의 id가 일치하는지 확인
        authimgboxValidator.ValidAuthImgBoxIdEqualSellerId(authimgbox.getAuth().getAuthseller().getId(), sellerId);

        // 둘 다 통과가 되면 true, false를 업데이트 해줌
        authimgbox.AuthBoolupdate(authBoolRequestDto);

    }
}
