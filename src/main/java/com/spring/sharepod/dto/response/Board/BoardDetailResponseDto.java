package com.spring.sharepod.dto.response.Board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDetailResponseDto {
    private String title;
    private String videourl;
    private String imgurl1;
    private String imgurl2;
    private String imgurl3;
    private String contents;
    private int originprice;
    private int dailyrentalfee;
    private String nickname;
    private String mapdata;
    private String category;
    private String boardquility;
    private boolean isliked;
    private int likecount;
    private String modifiedat;
    private String userimg;
}
