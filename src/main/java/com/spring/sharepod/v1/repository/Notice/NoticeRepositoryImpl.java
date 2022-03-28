package com.spring.sharepod.v1.repository.Notice;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.sharepod.v1.dto.response.NoticeInfoList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.sharepod.entity.QNotice.notice;
import static com.spring.sharepod.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NoticeInfoList> noticeInfoList(Long userId) {
        return getNoticeInfoList(userId).fetch();
    }

    private JPAQuery<NoticeInfoList> getNoticeInfoList(Long userId){
        return jpaQueryFactory.select(Projections.constructor(NoticeInfoList.class,
                notice.id,
                user.nickName,
                notice.noticeInfo
                )).from(notice)
                .rightJoin(user)
                .on(notice.receiver.id.eq(user.id).or(notice.sender.id.eq(user.id)))
                .where(notice.receiver.id.eq(userId).or(notice.sender.id.eq(userId)),
                        user.id.eq(userId));
    }

}
