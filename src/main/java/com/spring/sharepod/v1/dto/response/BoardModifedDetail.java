package com.spring.sharepod.v1.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardModifedDetail {
    private String firstImgUrl;
    private String secondImgUrl;
    private String lastImgUrl;
    private String videoUrl;
    private String title;
    private String contents;
    private int originPrice;
    private int dailyRentalFee;
    private String boardTag;
    private String boardRegion;
    private String category;
    private String productQuality;
    private LocalDateTime modifiedAt;
}
