package com.spring.sharepod.dto.response.Liked;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LikedResponseDto {
    private Long boardid;
    private String boardtitle;
    private Long userid;
    private String category;
}
