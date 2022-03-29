package com.spring.sharepod.v1.dto.response.Reservation;

import lombok.*;

import java.util.List;

public class ReservationResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationGetFinalDTO {
        private String result;
        private String msg;
        private List<ReservationGetDTO> reservationList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class accReservationDTO{
        private String result;
        private String msg;
        private Long boardId;
        private Long sellerId;
        private String buyerNickName;
        private boolean check;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReservationDeal {
        private String result;
        private String msg;
        private Long userId;
        private Long boardId;
        private String startRental;
        private String endRental;
    }



}
