package com.spring.sharepod.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VideoAllResponseDto {
    @JsonProperty("boardId")
    private Long id;
    private String boardRegion;
    private String title;
    private String videoUrl;
    private String userImg;
    private String nickName;
}
