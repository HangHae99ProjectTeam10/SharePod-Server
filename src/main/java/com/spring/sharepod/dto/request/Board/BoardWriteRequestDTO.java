package com.spring.sharepod.dto.request.Board;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardWriteRequestDTO {
    private String videourl;
    private String imgurl1;
    private String imgurl2;
    private String imgurl3;
    private Long userid;
    private String title;
    private String contents;
    private int originprice;
    private int dailyrentalfee;
    private String mapdata;
    private String category;
    private String boardquility;
}
