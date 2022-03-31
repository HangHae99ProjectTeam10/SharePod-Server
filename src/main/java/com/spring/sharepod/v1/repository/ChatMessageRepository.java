package com.spring.sharepod.v1.repository;

import com.spring.sharepod.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    @Query(nativeQuery = true, value = "select * from chat_message ch where ch.chatroomid=:chatroomId ORDER BY ch.modified_at desc LIMIT :startNum,5;")
    List<ChatMessage> findAllByChatRoomOrderByModifiedAt(Long chatroomId, int startNum);
}
