package com.spring.sharepod.dto.response.Reservation;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationGetDTO {
    private String nickname;
    private LocalDate rentalstart;
    private LocalDate rentalend;
    private String boardtitle;
    private Long boardid;
}
