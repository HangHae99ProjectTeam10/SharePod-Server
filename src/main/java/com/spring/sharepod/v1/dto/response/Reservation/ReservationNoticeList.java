package com.spring.sharepod.v1.dto.response.Reservation;

import com.spring.sharepod.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationNoticeList {
    private User buyer;
    private User seller;
}
