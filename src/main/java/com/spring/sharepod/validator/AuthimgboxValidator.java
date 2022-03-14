package com.spring.sharepod.validator;

import com.spring.sharepod.entity.Authimgbox;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.AuthimgboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.spring.sharepod.exception.ErrorCode.AUTHIMGBOX_NOT_EXIST;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class AuthimgboxValidator {
    private final AuthimgboxRepository authimgboxRepository;

    //
    public Authimgbox ValidAuthImgBoxByCheckId(Long ImgboxCheckId) {
        Authimgbox authimgbox = authimgboxRepository.findById(ImgboxCheckId).orElseThrow(() -> new ErrorCodeException(AUTHIMGBOX_NOT_EXIST));
        return authimgbox;
    }
}
