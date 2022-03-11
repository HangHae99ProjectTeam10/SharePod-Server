package com.spring.sharepod.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Column(nullable = false)
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

    //Reservation : User => 해당 userid의 요청 목록을 가져오기 위한 양방향 설정
    @OneToMany(mappedBy = "seller")
    private List<Reservation> reservation = new ArrayList<>();

    //Auth : User => 해당 authbuyer 요청 목록을 가져오기 위한 양방향 설정
    @OneToMany(mappedBy = "authbuyer")
    private List<Auth> authbuyerlist  = new ArrayList<>();

    //Auth : User => 해당 authseller 요청 목록을 가져오기 위한 양방향 설정
    @OneToMany(mappedBy = "authseller")
    private List<Auth> authsellerlist = new ArrayList<>();


}



