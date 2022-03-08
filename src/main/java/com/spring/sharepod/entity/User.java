package com.spring.sharepod.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //유저 아이디 (이메일 형태)
    @Column(nullable = false, unique = true)
    private String username;

    // 유저 비밀번호
    @Column(nullable = false, unique = true)
    private String password;

    //유저 닉네임
    @Column(nullable = false, unique = true)
    private  String nickname;

    // 유저 프로필 이미지
    @Column
    private String userimg;

    //유저 지역명
    @Column
    private String mapdata;

}



