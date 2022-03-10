package com.spring.sharepod.dto.response.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardAllResponseDto {

    private Long boardid;
    private String imgurl;
    private String title;
    private String category;
    private int dailyrentalfee;
    private String nickname;

    @Builder
    public BoardAllResponseDto(Long boardid, String imgurl, String title, String category, int dailyrentalfee, String nickname){
        this.boardid = boardid;
        this.imgurl = imgurl;
        this.title = title;
        this.category = category;
        this.dailyrentalfee = dailyrentalfee;
        this.nickname = nickname;
    }
}
