package com.spring.sharepod.service;

import com.sparta.springcore.dto.SignupRequestDto;
import com.sparta.springcore.model.User;
import com.sparta.springcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(SignupRequestDto requestDto) {
// 회원 ID 중복 확인
        System.out.println("aaaaab");
        String userId = requestDto.getUserId();

        Optional<User> found = userRepository.findByUserId(userId);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

// 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();



// 사용자 ROLE 확인

        User user = new User(userId, password, nickname);
        userRepository.save(user);








