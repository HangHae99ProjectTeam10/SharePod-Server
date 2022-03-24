package com.spring.sharepod.entity;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ChatMessage extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ChatMessage : User => N: 1 seller 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITERID")
    private User writer;

    //Liked : Board => N: 1 boardid 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHATROOMID")
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private String message;
}