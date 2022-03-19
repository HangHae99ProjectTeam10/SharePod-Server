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
public class AuthImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사진 url
    @Column(nullable = true)
    private String authImgUrl;

    // 사진 인증 확인
    @Column(nullable = false)
    private boolean checkImgBox;


    //Authimgbox : Auth => N: 1 엔티티에서 AUTHID 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHID")
    private Auth auth;

    //인증 업데이트
    public void AuthBoolupdate(Boolean check){
        this.checkImgBox = check;
    }

    public void updateImgUrl(String imgurl){
        this.authImgUrl = imgurl;
    }
}
