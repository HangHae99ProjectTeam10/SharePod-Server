package com.spring.sharepod.repository;

import com.spring.sharepod.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 회원가입 시, 닉네임 중복 확인
    Optional<User> findByNickname(String nickname);

    // 회원가입시 유저네임 중복 확인
    Optional<User> findByUsername(String username);

}
