package com.spring.sharepod.dto.response.Board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VideoAllResponseDto {
    private Long boardid;
    private String title;
    private String videourl;
    private String userimg;
    private String nickname;

}
