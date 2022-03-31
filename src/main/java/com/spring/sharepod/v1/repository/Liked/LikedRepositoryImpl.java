package com.spring.sharepod.v1.repository.Liked;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.spring.sharepod.entity.QLiked.liked;


@Repository
@RequiredArgsConstructor
public class LikedRepositoryImpl implements LikedRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean existLikedBoolean(Long userId, Long boardId) {
        return getLikedByBoolean(userId,boardId);
    }

    private Boolean getLikedByBoolean(Long userId, Long boardId){
        return jpaQueryFactory.
                select(liked.id)
                .from(liked)
                .where(liked.user.id.eq(userId),liked.board.id.eq(boardId))
                .fetchFirst() != null;
    }
}
