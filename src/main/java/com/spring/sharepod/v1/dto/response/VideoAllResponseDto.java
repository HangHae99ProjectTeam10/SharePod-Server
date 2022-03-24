package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VideoAllResponseDto {
    private Long boardId;
    private String videoUrl;
    private String userImg;
    private String nickName;
}
