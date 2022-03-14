package com.spring.sharepod.validator;

import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.ErrorCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.spring.sharepod.exception.ErrorCode.USER_NOT_FOUND;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class TokenValidator {
    //userid와 토큰 비교
    public void userIdCompareToken(Long userid) {
//        if(userid != user.getid){
//            throw new ErrorCodeException(USER_NOT_FOUND);
//        }
    }
    public void likeIdCompareToken(Long userid, User user){
        if(!Objects.equals(user.getId(), userid)){
            throw new ErrorCodeException(USER_NOT_FOUND);
        }
    }

}
