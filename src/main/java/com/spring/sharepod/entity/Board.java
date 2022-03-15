package com.spring.sharepod.entity;


import com.spring.sharepod.dto.request.Board.BoardPatchRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시물 제목
    @Column(nullable = false)
    private String title;

    // 게시물 영상 url
    @Column(nullable = false , unique = true)
    private String videourl;

    //게시물 사진 1 url
    @Column(nullable = false, unique = true)
    private String imgurl1;
    //게시물 사진 2 url
    @Column(nullable = false, unique = true)
    private String imgurl2;
    //게시물 사진 3 url
    @Column(nullable = false, unique = true)
    private String imgurl3;

    // 게시물 내용
    @Column(nullable = false)
    private String contents;

    // 원가
    @Column(nullable = false)
    private int originprice;
    // 하루대여 가격
    @Column(nullable = false)
    private int dailyrentalfee;

    // 카테고리
    @Column(nullable = false)
    private String category;

    //상품 등록
    @Column(nullable = false)
    private String mapdata;

    //상품 품질
    @Column(nullable = false)
    private String boardquility;

    //게시판 활성화 변수
    @Column(nullable = false)
    private Boolean appear;

    //Board : User => N: 1 엔티티에서 userid 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERID")
    private User user;

    @OneToOne(mappedBy = "board", cascade = CascadeType.REMOVE)
    private Auth auth;

    //Board : Liked => 해당 boardid를 좋아요 누른 목록을 가져오기 위한 양방향 설정
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Liked> likeNumber = new ArrayList<>();


    public void setAppear(boolean appear){
        this.appear = appear;
    }

    //게시판 업데이트
    public void update(BoardPatchRequestDTO patchRequestDTO){
        this.title = patchRequestDTO.getTitle();
        this.videourl = patchRequestDTO.getVideourl();
        this.imgurl1 = patchRequestDTO.getImgurl1();
        this.imgurl2 = patchRequestDTO.getImgurl2();
        this.imgurl3 = patchRequestDTO.getImgurl3();
        this.contents = patchRequestDTO.getContents();
        this.originprice = patchRequestDTO.getOriginprice();
        this.dailyrentalfee = patchRequestDTO.getDailyrentalfee();
        this.mapdata = patchRequestDTO.getMapdata();
        this.category = patchRequestDTO.getCategory();
        this.boardquility = patchRequestDTO.getBoardquility();
    }
}