package com.spring.sharepod.v1.repository.Reservation;

import com.spring.sharepod.v1.dto.response.ReservationGetDTO;
import com.spring.sharepod.v1.dto.response.ReservationNoticeList;

import java.util.List;

public interface ReservationRepositoryCustom {

    List<ReservationGetDTO> reservationList(Long sellerId);

    List<ReservationNoticeList> reservationNoticeList(Long reservationId);
}
