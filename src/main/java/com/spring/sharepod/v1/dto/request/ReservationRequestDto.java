package com.spring.sharepod.v1.dto.request;

import lombok.*;

public class ReservationRequestDto {

    @Getter
    @NoArgsConstructor
    @Setter
    public static class AcceptOrNot {
        private Long sellerId;
        private String buyerNickname;
        private String startRental;
        private String endRental;
        private boolean check;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Reservation{
        private Long userId;
        private String startRental;
        private String endRental;
    }



}
