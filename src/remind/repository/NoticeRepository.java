package com.spring.sharepod.repository;

import com.spring.sharepod.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    //알림 갯수
    @Query(nativeQuery = true, value = "select COUNT(n.id) from notice n where n.buyer=:userid or n.seller=:userid")
    int findByCOUNTBuyerOrSellerId(Long userid);

    //알림 목록
    @Query("select n from Notice n where n.buyer.id=:userid or n.seller.id=:userid")
    List<Notice> findByBuyerOrSellerId(Long userid);


    @Modifying
    @Transactional
    @Query("delete from Notice n where n.id=:noticeid")
    void deleteByNoticeId(Long noticeid);


    @Query("select n from Notice n where n.buyer.id=:userid or n.seller.id=:userid")
    Notice findByUserId(Long userid);
}
