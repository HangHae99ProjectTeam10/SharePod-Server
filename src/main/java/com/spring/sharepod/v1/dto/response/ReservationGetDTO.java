package com.spring.sharepod.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationGetDTO {
    private String nickName;

    private LocalDate startRental;
    private LocalDate endRental;

    @JsonProperty(value = "boardTitle")
    private String title;

    @JsonProperty(value = "boardId")
    private Long id;

    @JsonProperty(value = "boardImg")
    private String firstImgUrl;
}