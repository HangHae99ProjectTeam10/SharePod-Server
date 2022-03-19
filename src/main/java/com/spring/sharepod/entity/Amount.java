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
public class Amount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "BOARDID")
    private Board board;

    // 원가
    @Column(nullable = false)
    private int originPrice;

    // 하루대여 가격
    @Column(nullable = false)
    private int dailyRentalFee;

    //게시글 금액 수정
    public void updateAmount(int originPrice, int dailyRentalFee) {
        this.originPrice = originPrice;
        this.dailyRentalFee = dailyRentalFee;
    }

}
