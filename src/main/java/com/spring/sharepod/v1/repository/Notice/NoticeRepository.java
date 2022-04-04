package com.spring.sharepod.v1.repository.Notice;

import com.spring.sharepod.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface NoticeRepository extends JpaRepository<Notice, Long>,NoticeRepositoryCustom {

    //24번 API 알림 갯수
    @Query(nativeQuery = true, value = "select COUNT(n.id) from notice n where n.receiverid=:userid")
    int findByCOUNTBuyerOrSellerId(Long userid);

    //알림 목록
    @Query(nativeQuery = true, value ="select exists (SELECT n.id from notice n where n.receiverid=:userId)")
    int findByRecieverId(Long userId);

    //26번 API 알림 확인 or 삭제
    @Modifying
    @Transactional
    @Query("delete from Notice n where n.id=:noticeid")
    void deleteByNoticeId(Long noticeid);
}
