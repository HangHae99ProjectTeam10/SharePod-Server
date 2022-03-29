package com.spring.sharepod.v1.repository.Reservation;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.sharepod.v1.dto.response.ReservationGetDTO;
import com.spring.sharepod.v1.dto.response.ReservationNoticeList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.sharepod.entity.QBoard.board;
import static com.spring.sharepod.entity.QImgFiles.imgFiles;
import static com.spring.sharepod.entity.QReservation.reservation;
import static com.spring.sharepod.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;


    //18번
    @Override
    public List<ReservationGetDTO> reservationList(Long sellerId) {
        return getReservationList(sellerId).fetch();
    }

    //19반
    @Override
    public List<ReservationNoticeList> reservationNoticeList(Long reservationId) {
        return getNoticeReservationList(reservationId).fetch();
    }


    //거래 요청 목록 리스트(여기에 user를 어떻게 받아야할지 현재는 reservation.buyer.nickName을 받지만 user.nickName으로 받고 싶다)
    private JPAQuery<ReservationGetDTO> getReservationList(Long sellerId) {
        return jpaQueryFactory.select(Projections.constructor(ReservationGetDTO.class,
                        user.nickName,
                        reservation.startRental,
                        reservation.endRental,
                        board.title,
                        board.id,
                        imgFiles.firstImgUrl
                ))
                .from(reservation)
                .innerJoin(board)
                .on(reservation.board.id.eq(board.id))
                .innerJoin(imgFiles)
                .on(board.id.eq(imgFiles.board.id))
                .innerJoin(user)
                .on(reservation.buyer.id.eq(user.id))
                .where(reservation.seller.id.eq(sellerId))
                .orderBy(board.modifiedAt.desc());
    }

//    //비교 버전 거래 요청 목록 리스트(여기에 user를 어떻게 받아야할지 현재는 reservation.buyer.nickName을 받지만 user.nickName으로 받고 싶다)
//    private JPAQuery<ReservationGetDTO> getReservationList(Long sellerId) {
//        return jpaQueryFactory.select(Projections.constructor(ReservationGetDTO.class,
//                        reservation.seller.nickName,
//                        reservation.startRental,
//                        reservation.endRental,
//                        reservation.board.title,
//                        reservation.board.id,
//                        reservation.board.imgFiles.firstImgUrl
//                ))
//                .from(reservation)
////                .innerJoin(board)
//                .where(reservation.seller.id.eq(sellerId))
//                .orderBy(board.modifiedAt.desc());
//    }

    //19번
    private JPAQuery<ReservationNoticeList> getNoticeReservationList(Long reservationId) {
        return jpaQueryFactory.select(Projections.constructor(ReservationNoticeList.class,
                        reservation.buyer,
                        reservation.seller
                )).from(reservation)
                .where(reservation.id.eq(reservationId));
    }


}
