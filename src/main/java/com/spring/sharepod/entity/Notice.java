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
    private String noticeInfo;

    //Notice : User => N: 1 receiver 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "RECEIVERID")
    private User receiver;

    //Notice : User => N: 1 sender 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDERID")
    private User sender;

    //Notice : User => N: 1 seller 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARDID")
    private Board board;
}
