package com.spring.sharepod.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatRoom extends Timestamped implements Serializable { // redis에 저장되는 객체들은 Serialize가 가능해야 함, -> Serializable 참조
    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ChatRoom : User => N: 1 seller 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLERID")
    private User seller;

    //ChatRoom : User => N: 1 buyer 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYERID")
    private User buyer;

    //ChatRoom : Board => N: 1 boardid 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARDID")
    private Board board;


    //ChatRoom: ChatMessage 1:N
    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatMessage> chatMessageList = new ArrayList<>();


    public static ChatRoom create(ChatRoom create) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.seller = create.getSeller();
        chatRoom.buyer = create.getBuyer();
        chatRoom.board = create.getBoard();
        return chatRoom;
    }
}
