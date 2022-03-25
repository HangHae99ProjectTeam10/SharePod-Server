package com.spring.sharepod.v1.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBoardResponseDto {
    private Long boardId;
    private String boardTitle;
    private String boardTag;
    private String boardRegion;
    private String FirstImg;
    private LocalDateTime modifiedAt;
    private int dailyRentalFee;
    private String nickName;
    private String category;
    private Optional<Boolean> isLiked;
}
