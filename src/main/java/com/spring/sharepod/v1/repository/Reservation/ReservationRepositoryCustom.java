package com.spring.sharepod.v1.repository.Reservation;

import com.spring.sharepod.v1.dto.response.Reservation.ReservationGetDTO;
import com.spring.sharepod.v1.dto.response.Reservation.ReservationNoticeList;

import java.util.List;

public interface ReservationRepositoryCustom {

    //18번 API 거래 요청 목록(현재 접속한 사람에게 온 요청 목록)
    List<ReservationGetDTO> reservationList(Long sellerId);

    List<ReservationNoticeList> reservationNoticeList(Long reservationId);
}
