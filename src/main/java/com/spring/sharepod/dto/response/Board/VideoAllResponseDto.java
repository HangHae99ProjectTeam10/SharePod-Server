package com.spring.sharepod.dto.response.Board;


import lombok.Builder;
import lombok.Getter;

@Getter
public class VideoAllResponseDto {
    private Long boardid;
    private String title;
    private String videourl;
    private String userimg;
    private String nickname;

    @Builder
    public VideoAllResponseDto(Long boardid, String title, String videourl, String userimg, String nickname){
        this.boardid = boardid;
        this.title = title;
        this.videourl =videourl;
        this.userimg = userimg;
        this.nickname = nickname;
    }
}
