package com.spring.sharepod.v1.repository.Notice;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.sharepod.v1.dto.response.notice.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.sharepod.entity.QBoard.board;
import static com.spring.sharepod.entity.QNotice.notice;
import static com.spring.sharepod.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notice> noticeInfoList(Long userId) {
        return getNoticeInfoList(userId).fetch();
    }

    private JPAQuery<Notice> getNoticeInfoList(Long userId) {
        List<Long> ids = jpaQueryFactory
                .select(user.id)
                .from(user)
                .where(user.id.eq(userId))
                .fetch();

        return jpaQueryFactory.select(Projections.constructor(Notice.class,
                        notice.id,
                        user.nickName,
                        user.userRegion,
                        user.userImg,
                        notice.noticeInfo,
                        board.id,
                        notice.isChat

                )).from(notice)
                .innerJoin(user)
                .on(notice.receiver.id.eq(user.id))
                .innerJoin(board)
                .on(board.id.eq(notice.board.id))
                .where(notice.receiver.id.in(ids));


                //.where(notice.receiver.id.eq(userId));
//                .where(notice.receiver.id.in(JPAExpressions.select(notice.receiver.id)
//                        .from(notice)
//                        .where(notice.receiver.id.eq(userId))
//                ));

    }
}
