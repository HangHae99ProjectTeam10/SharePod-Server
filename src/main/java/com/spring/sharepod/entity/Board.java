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
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 게시물 제목

    @Column(nullable = false , unique = true)
    private String videourl; // 게시물 영상 url

    @Column(nullable = false, unique = true)
    private  String imgurl1; //게시물 사진 1 url
    @Column(nullable = false, unique = true)
    private  String imgurl2; //게시물 사진 2 url
    @Column(nullable = false, unique = true)
    private  String imgurl3; //게시물 사진 3 url

    @Column(nullable = false)
    private  String contents; // 게시물 내용

    @Column
    private String userimg; // 유저 프로필 이미지

    @Column
    private String mapdata; //유저 지역명

}
