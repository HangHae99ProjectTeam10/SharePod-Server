package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.Auth.AuthBoolRequestDto;
import com.spring.sharepod.entity.Authimgbox;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.model.AuthImg;
import com.spring.sharepod.repository.AuthimgboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.spring.sharepod.exception.ErrorCode.AUTHIMGBOX_NOT_EXIST;
import static com.spring.sharepod.exception.ErrorCode.AUTHIMGBOX_NOT_SELLER;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthimgboxRepository authimgboxRepository;

    //빌려준 사람만의 기능, 인증 성공 or 실패
    @Transactional
    public void BoolAuth(AuthBoolRequestDto authBoolRequestDto) {
        //주어진 id에 대해서 imgbox가 존재하는지 확인
        Authimgbox authimgbox = authimgboxRepository.findById(authBoolRequestDto.getImgboxcheckid()).orElseThrow(() -> new ErrorCodeException(AUTHIMGBOX_NOT_EXIST));

        //해당에서 받아온 seller의 id와 보내준 seller의 id가 일치하는지 확인
        if (!Objects.equals(authimgbox.getAuth().getAuthseller().getId(), authBoolRequestDto.getSellerid())){
            throw new ErrorCodeException(AUTHIMGBOX_NOT_SELLER);
        }else{
          // 둘 다 통과가 되면 true, false를 업데이트 해줌
            authimgbox.AuthBoolupdate(authBoolRequestDto);
        }
    }


}
