package com.spring.sharepod.v1.dto.response.Board;

import com.querydsl.core.types.dsl.NumberPath;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BoardAllResponseDto {

    private Long id;
    private String firstImgUrl;
    private String title;
    private String category;
    private int dailyRentalFee;
    private String boardRegion;
    private String boardTag;
    private LocalDateTime modifiedAt;
    private Optional<Boolean> isLiked;

}
