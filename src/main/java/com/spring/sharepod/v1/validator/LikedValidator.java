package com.spring.sharepod.v1.validator;

import com.spring.sharepod.v1.repository.LikedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class LikedValidator {
    private final LikedRepository likedRepository;

}
