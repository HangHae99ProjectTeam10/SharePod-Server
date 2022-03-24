package com.spring.sharepod.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class ImgFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "BORDID")
    private Board board;

    //게시물 사진 1 url
    @Column
    private String firstImgUrl;
    //게시물 사진 2 url
    @Column
    private String secondImgUrl;
    //게시물 사진 3 url
    @Column
    private String lastImgUrl;

    // 게시물 영상 url
    @Column
    private String videoUrl;

    //게시판 이미지 업데이트
    public void updateImgFiles(String firstImgUrl, String secondImgUrl, String lastImgUrl, String videoUrl) {
        this.firstImgUrl = firstImgUrl;
        this.secondImgUrl = secondImgUrl;
        this.lastImgUrl = lastImgUrl;
        this.videoUrl = videoUrl;
    }
}
