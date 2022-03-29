package com.spring.sharepod.v1.dto.response.Liked;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class LikedListResponseDto {
    private Long boardId;
    private String boardTitle;
    private String boardRegion;
    private String boardTag;
    private String FirstImg;
    private Boolean isliked;
    private LocalDateTime modifiedAt;
    private int dailyRentalFee;
    private String userNickName;
    private String category;
}
