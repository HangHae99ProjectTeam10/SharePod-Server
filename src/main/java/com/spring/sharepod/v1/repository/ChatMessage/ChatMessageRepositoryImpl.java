package com.spring.sharepod.v1.repository.ChatMessage;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.sharepod.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.spring.sharepod.entity.QChatMessage.chatMessage;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatMessage> findByChatRoomOrderByModifiedAt(Long chatRoomId, LocalDateTime localDateTime) {
        return getChatMessages(chatRoomId,localDateTime).fetch();
    }

    private JPAQuery<ChatMessage> getChatMessages(Long chatRoomId,LocalDateTime localDateTime){
        return jpaQueryFactory.selectFrom(chatMessage).where(chatMessage.chatRoom.id.eq(chatRoomId),
                startDate(localDateTime)
                )
                .orderBy(chatMessage.modifiedAt.desc())
                .limit(5);
    }

    private BooleanExpression startDate(LocalDateTime localDateTime) {
        return localDateTime != null ? chatMessage.modifiedAt.lt(localDateTime) : null;
    }
}
