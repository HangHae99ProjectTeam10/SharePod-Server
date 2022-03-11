package com.spring.sharepod.dto.response.Reservation;


import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationGetFinalDTO {
    private String result;
    private List<ReservationGetDTO> reservation;
}
