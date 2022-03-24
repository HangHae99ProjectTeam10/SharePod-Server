package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
public  class RentSeller {
    private Long boardId;
    private String boardTitle;
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


//        public static RentSeller toDto(Board board){
//
//        }
}
