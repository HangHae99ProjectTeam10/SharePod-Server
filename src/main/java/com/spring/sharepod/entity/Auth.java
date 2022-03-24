package com.spring.sharepod.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Auth{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //인증 확인 변수
    @Column(nullable = false)
    private boolean sellectAllImg;

    //대여 시작 날짜
    @Column(nullable = false)
    private LocalDate startRental;

    //대여 끝나는 날짜
    @Column(nullable = false)
    private LocalDate endRental;

    //Auth : User => N: 1 buyer 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHBUYER")
    private User authBuyer;

    //Auth : User => N: 1 buyer 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHSELLER")
    private User authSeller;

    //Auth : Board => 1: 1 boardid 외래키를 뜻함
    @OneToOne
    @JoinColumn(name = "BOARDID")
    private Board board;

    //Auth : Authimgbox => 해당 authid에 소속된 목록을 가져오기 위한 양방향 설정
    @Builder.Default
    @OneToMany(mappedBy = "auth" ,cascade = CascadeType.REMOVE)
    private List<AuthImg> authImgList = new ArrayList<>();


    //모든 동의가 끝났을 시점의 sellectAllImg true로
    public void setSellectAllImg(Boolean sellectAllImg){
        this.sellectAllImg = sellectAllImg;
    }


}