package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BoardDetails {
    private String firstImg;
    private String secondImg;
    private String lastImg;
    private String videoUrl;
    private String boardTitle;
    private String boardContents;
    private int originPrice;
    private int dailyRentalFee;
    private String boardTag;
    private String userNickName;
    private String sellerRegion;
    private String boardRegion;
    private String category;
    private String productQuality;
    private int likeNumberSize;
    private String sellerImg;
    private LocalDateTime modifiedAt;

}
