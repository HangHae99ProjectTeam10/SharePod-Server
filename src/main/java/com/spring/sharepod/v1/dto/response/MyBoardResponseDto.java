package com.spring.sharepod.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBoardResponseDto {
    @JsonProperty(value="boardId")
    private Long id;

    @JsonProperty(value="boardTitle")
    private String title;

    private String boardTag;
    private String boardRegion;

    @JsonProperty(value="firstImg")
    private String firstImgUrl;

    private LocalDateTime modifiedAt;
    private int dailyRentalFee;
    private String nickName;
    private String category;
    private Optional<Boolean> isLiked;
}
