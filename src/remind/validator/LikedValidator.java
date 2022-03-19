package com.spring.sharepod.validator;

import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.repository.LikedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class LikedValidator {
    private final LikedRepository likedRepository;

}
