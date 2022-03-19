package com.spring.sharepod.v1.validator;

import com.spring.sharepod.entity.AuthImg;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.repository.AuthImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.spring.sharepod.exception.CommonError.ErrorCode.AUTHIMGBOX_NOT_EXIST;
import static com.spring.sharepod.exception.CommonError.ErrorCode.AUTHIMGBOX_NOT_SELLER;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class AuthImgValidator {
    private final AuthImgRepository authImgRepository;

    //imgbox의 존재 확인
    public AuthImg ValidAuthImgById(Long authImgId) {
        AuthImg authImg = authImgRepository.findById(authImgId).orElseThrow(() -> new ErrorCodeException(AUTHIMGBOX_NOT_EXIST));
        return authImg;
    }

    //구매자가 아닐 경우
    public AuthImg ValidAuthImgByBuyerId(Long buyerId, Long authImgId) {
        AuthImg authImg = authImgRepository.findByBuyerAndAuthImgId(buyerId, authImgId).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.AUTHIMGBOX_NOT_BUYER));
        return authImg;
    }


    //구매자가 일치하지 않을 경우
    public void ValidAuthImgBoxIdEqualBuyerId(Long authimgboxbuyerid,Long requestuserid){
        //해당에서 받아온 seller의 id와 보내준 seller의 id가 일치하는지 확인
        if (!Objects.equals(authimgboxbuyerid, requestuserid)){
            throw new ErrorCodeException(AUTHIMGBOX_NOT_SELLER);
        }
    }


    //판매자가 일치하지 않을 경우
    public void ValidAuthImgBoxIdEqualSellerId(Long authimgboxsellerid, Long requestsellerid){
        //해당에서 받아온 seller의 id와 보내준 seller의 id가 일치하는지 확인
        if (!Objects.equals(authimgboxsellerid, requestsellerid)){
            throw new ErrorCodeException(AUTHIMGBOX_NOT_SELLER);
        }
    }
}
