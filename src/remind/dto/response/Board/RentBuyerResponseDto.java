package com.spring.sharepod.dto.response.Board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RentBuyerResponseDto {
    private Long boardid;
    private String boardtitle;
    private String nickname;
    private String category;
    private Long authid;
}
