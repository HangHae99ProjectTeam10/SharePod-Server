package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.UserRegisterRequestDto;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // private final RegisterValidator registerValidator;
//    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 회원가입
    @Transactional
    public Long createUser(UserRegisterRequestDto userRegisterRequestDto) {
        //회원가입 유효성 검사 validator 통해서 검증한다. 중간에 이상하거 있으면 바로 거기서 메시지 반환하도록
        //registerValidator.validateUserRegisterData(userRegisterRequestDto);

        // 여기서부터는 검증된 데이터들이기에 그냥 비밀번호 암호화하고 빌더 패턴으로 유저에 대한 정보를 생성한다.
        // 비밀번호 암호화
        //String password = passwordEncoder.encode(userRegisterRequestDto.getPassword());

        // 유저 생성
        User user = User.builder()
                .username(userRegisterRequestDto.getUsername())
                .password(userRegisterRequestDto.getPassword())
                .mapdata(userRegisterRequestDto.getMapdata())
                .nickname(userRegisterRequestDto.getNickname())
                //.roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build();

        // 유저 저장하기
        userRepository.save(user);

        return user.getId();
    }

}
