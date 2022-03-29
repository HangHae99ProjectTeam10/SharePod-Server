package com.spring.sharepod.v1.dto.response.User;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReservation {
    @JsonProperty(value="boardId")
    private Long id;

    @JsonProperty(value="boardTitle")
    private String title;
    private String boardRegion;
    private String boardTag;
    private String firstImgUrl;
    private int dailyRentalFee;
    private LocalDate startRental;
    private LocalDate endRental;
    private String nickName;

    private String category;
    private Long reservationId;
    private Optional<Boolean> isLiked;

}
