package com.spring.sharepod.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //대여 시작 날짜
    @Column(nullable = false)
    private LocalDate rentalstart;

    //대여 끝나는 날짜
    @Column(nullable = false)
    private LocalDate rentalend;

    //Reservation : User => N: 1 buyer 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYER")
    private User buyer;

    //Reservation : User => N: 1 seller 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER")
    private User seller;

    //Reservation : Board => N: 1 boardid 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARDID")
    private Board board;
}
