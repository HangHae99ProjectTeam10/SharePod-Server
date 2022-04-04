package com.spring.sharepod.v1.repository.ChatMessage;

import com.spring.sharepod.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepositoryCustom {

    List<ChatMessage> findByChatRoomOrderByModifiedAt(Long chatRoomId, LocalDateTime localDateTime);
}
