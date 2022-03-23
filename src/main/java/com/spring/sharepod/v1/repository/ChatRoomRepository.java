package com.spring.sharepod.v1.repository;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.ChatRoom;
import com.spring.sharepod.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query(nativeQuery = true, value = "select * from chat_room cr where cr.buyerid=:userId or cr.sellerid=:userId ORDER BY cr.modified_at asc")
    List<ChatRoom> findAllByUserId(Long userId);


    ChatRoom findByBuyerAndAndBoard(User buyer, Board board);


}
