package com.spring.sharepod.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Reservation : User => N: 1 buyer 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYERID")
    private User buyer;

    //Reservation : User => N: 1 seller 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLERId")
    private User seller;

    //Reservation : Board => N: 1 boardid 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARDID")
    private Board board;

    //대여 시작 날짜
    @Column(nullable = false)
    private LocalDate startRental;

    //대여 끝나는 날짜
    @Column(nullable = false)
    private LocalDate endRental;
}
