package com.spring.sharepod.v1.repository.ChatMessage;

import com.spring.sharepod.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long>, ChatMessageRepositoryCustom{

    // 29번 해당 채팅방 채팅내용 반환
    @Query(nativeQuery = true, value = "select * from chat_message ch where ch.chatroomid=:chatroomId and ORDER BY ch.modified_at desc LIMIT :startNum,5;")
    List<ChatMessage> findAllByChatRoomOrderByModifiedAt(Long chatroomId, int startNum);


    void deleteAllByModifiedAtBefore(LocalDateTime settime);

}
