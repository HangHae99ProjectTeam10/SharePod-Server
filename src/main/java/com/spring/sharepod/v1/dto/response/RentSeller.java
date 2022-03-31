package com.spring.sharepod.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentSeller {
    @JsonProperty(value="boardId")
    private Long id;

    @JsonProperty(value="boardTitle")
    private String title;
    private String boardRegion;
    private String boardTag;
    private String FirstImgUrl;
    private int dailyRentalFee;
    private LocalDate startRental;
    private LocalDate endRental;
    private String nickName;
    private String category;
    private Long authId;
    private Optional<Boolean> isLiked;
}
