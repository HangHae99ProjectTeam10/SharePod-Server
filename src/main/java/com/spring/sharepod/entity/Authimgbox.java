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
public class Authimgbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시물 제목
    @Column(nullable = false, unique = true)
    private String imgurl;

    //Authimgbox : Auth => N: 1 엔티티에서 AUTHID 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHID")
    private Auth auth;
}
