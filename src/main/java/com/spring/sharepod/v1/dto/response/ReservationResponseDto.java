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
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationGetFinalDTO {
        private String result;
        private List<ReservationGetDTO> reservationList;
    }

}
