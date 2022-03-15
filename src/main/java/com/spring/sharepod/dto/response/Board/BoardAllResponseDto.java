package com.spring.sharepod.dto.response.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardAllResponseDto {

    private Long boardid;
    private String imgurl;
    private String title;
    private String category;
    private int dailyrentalfee;
    private String nickname;

}
