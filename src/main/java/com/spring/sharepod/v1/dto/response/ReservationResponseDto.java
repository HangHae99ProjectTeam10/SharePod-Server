package com.spring.sharepod.v1.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ReservationResponseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationGetDTO {
        private String nickName;
        private LocalDate startRental;
        private LocalDate endRental;
        private String boardTitle;
        private Long boardId;
        private String boardImg;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationGetFinalDTO {
        private String result;
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
