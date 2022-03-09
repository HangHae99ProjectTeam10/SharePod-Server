package com.spring.sharepod.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
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
    private String modifiedat;
    private String userimg;

    @Builder
    public BoardDetailResponseDto(String title, String videourl, String imgurl1, String imgurl2, String imgurl3, String contents, int originprice, int dailyrentalfee, String nickname, String mapdata, String category, String boardquility, Boolean isliked, String modifiedat, String userimg){
        this.title = title;
        this.videourl = videourl;
        this.imgurl1 = imgurl1;
        this.imgurl2 = imgurl2;
        this.imgurl3 = imgurl3;
        this.contents = contents;
        this.originprice = originprice;
        this.dailyrentalfee = dailyrentalfee;
        this.nickname = nickname;
        this.mapdata = mapdata;
        this.userimg = userimg;
        this.category =category;
        this.boardquility = boardquility;
        this.isliked = isliked;
        this.modifiedat = modifiedat;
    }

}
