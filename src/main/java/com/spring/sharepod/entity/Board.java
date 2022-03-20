package com.spring.sharepod.entity;


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

    // 게시물 내용
    @Column(nullable = false)
    private String contents;

    // 카테고리
    @Column(nullable = false)
    private String category;

    //상품 등록
    @Column(nullable = false)
    private String boardRegion;

    //상품 품질
    @Column(nullable = false)
    private String productQuality;

    //게시판 활성화 변수
    @Column(nullable = false)
    private Boolean mainAppear;

    //게시판 태그
    @Column
    private String boardTag;


    //Board : User => N: 1 엔티티에서 userid 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERID")
    private User user;

    //Board : ImgFiles => 1 : 1 엔터티
    @OneToOne(mappedBy = "board", cascade = CascadeType.REMOVE)
    private ImgFiles imgFiles;

    //Board : Auth => 1 : 1 엔터티
    @OneToOne(mappedBy = "board", cascade = CascadeType.REMOVE)
    private Auth auth;

    //Board : Amount => 1 : 1 엔터티
    @OneToOne(mappedBy = "board", cascade = CascadeType.REMOVE)
    private Amount amount;


    //Board : Liked => 해당 boardid를 좋아요 누른 목록을 가져오기 위한 양방향 설정
    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Liked> likeNumber = new ArrayList<>();


    //메인 페이지에 보이게 하려면 setApper를 해주는 작업
    public void setAppear(boolean mainAppear) {
        this.mainAppear = mainAppear;
    }

    //게시판 업데이트
    public void updateBoard(String title, String contents, String category, String boardRegion, String productQuality, String boardTag) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.boardRegion = boardRegion;
        this.productQuality = productQuality;
        this.boardTag = boardTag;
    }
}