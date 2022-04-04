package com.spring.sharepod.v1.repository;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.ChatRoom;
import com.spring.sharepod.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 27번 채팅방 생성
    ChatRoom findByBuyerAndAndBoard(User buyer, Board board);

    // 28번 채팅 리스트 받아오기
    @Query(nativeQuery = true, value = "select * from chat_room cr where cr.buyerid=:userId or cr.sellerid=:userId ORDER BY cr.modified_at desc")
    List<ChatRoom> findAllByUserId(Long userId);


    List<ChatRoom> findAllByBuyerIdOrSellerId(Long buyerId,Long sellerId);

}
