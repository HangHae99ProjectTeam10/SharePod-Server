package com.spring.sharepod.dto.request.Reservation;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ReservationAcceptNotDTO {
    private Long seller;
    private String nickname;
    private String rentalstart;
    private String rentalend;
    private boolean check;

}
