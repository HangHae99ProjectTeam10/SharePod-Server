package com.spring.sharepod.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class TokenValidator {
    //userid와 토큰 비교
    public void userIdCompareToken(Long userid) {
//        if(userid != user.getid){
//            throw new ErrorCodeException(USER_NOT_FOUND);
//        }
    }
}
