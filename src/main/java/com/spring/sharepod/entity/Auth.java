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
public class Auth extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //인증 확인 변수
    @Column(nullable = false)
    private boolean check;

    //인증 사진 url
    @Column(nullable = false, unique = true)
    private String imgurl;

    //Auth : User => N: 1 buyer 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYER")
    private User user;

    //Auth : Board => N: 1 boardid 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARDID")
    private Board board;
}
