package com.spring.sharepod.v1.repository.Reservation;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.sharepod.v1.dto.response.ReservationGetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.sharepod.entity.QBoard.board;
import static com.spring.sharepod.entity.QImgFiles.imgFiles;
import static com.spring.sharepod.entity.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;


    //18번
    @Override
    public List<ReservationGetDTO> reservationList(Long sellerId) {
        return getReservationList(sellerId).fetch();
    }



    //거래 요청 목록 리스트(여기에 user를 어떻게 받아야할지 현재는 reservation.buyer.nickName을 받지만 user.nickName으로 받고 싶다)
    private JPAQuery<ReservationGetDTO> getReservationList(Long sellerId) {
        return jpaQueryFactory.select(Projections.bean(ReservationGetDTO.class,
                        reservation.buyer.nickName,
                        reservation.startRental,
                        reservation.endRental,
                        board.title,
                        board.id,
                        imgFiles.firstImgUrl
                ))
                .from(reservation)
                .rightJoin(board)
                .on(reservation.board.id.eq(board.id))
                .innerJoin(imgFiles)
                .on(board.id.eq(imgFiles.board.id))
//                .innerJoin(amount)
//                .on(imgFiles.board.id.eq(amount.board.id))
                .where(reservation.seller.id.eq(sellerId))
                .orderBy(board.modifiedAt.desc());
    }
}
