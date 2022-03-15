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
public class Notice extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //알림 종류
    @Column(nullable = false)
    private String noticeinfo;

    //Notice : User => N: 1 buyer 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "BUYER")
    private User buyer;

    //Notice : User => N: 1 seller 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER")
    private User seller;
}
