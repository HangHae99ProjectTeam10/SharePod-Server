package com.spring.sharepod.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private boolean checkorder;

    //대여 시작 날짜
    @Column(nullable = false)
    private LocalDate rentalstart;

    //대여 끝나는 날짜
    @Column(nullable = false)
    private LocalDate rentalend;

    //Auth : User => N: 1 buyer 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHBUYER")
    private User authbuyer;

    //Auth : User => N: 1 buyer 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHSELLER")
    private User authseller;

    //Auth : Board => N: 1 boardid 외래키를 뜻함
    @OneToOne
    @JoinColumn(name = "BOARDID")
    private Board board;

    //Auth : Authimgbox => 해당 authid에 소속된 목록을 가져오기 위한 양방향 설정
    @OneToMany(mappedBy = "auth")
    private List<Authimgbox> authimgboxList = new ArrayList<>();


}