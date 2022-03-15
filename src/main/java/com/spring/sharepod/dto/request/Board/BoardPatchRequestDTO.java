package com.spring.sharepod.dto.request.Board;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class BoardPatchRequestDTO {
    private Long userid;
    private String title;
    private String videourl;
    private String imgurl1;
    private String imgurl2;
    private String imgurl3;
    private String contents;
    private int originprice;
    private int dailyrentalfee;
    private String mapdata;
    private String category;
    private String boardquility;
}
