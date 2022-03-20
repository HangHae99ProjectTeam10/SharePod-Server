package com.spring.sharepod.v1.repository;

import com.spring.sharepod.entity.Auth;
import com.spring.sharepod.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    @Query("select a from Auth a where a.authBuyer.id=:buyerid ORDER BY a.endRental asc")
    List<Auth> findByBuyerId(Long buyerid);

    @Query("select a from Auth a where a.authSeller.id=:sellerid ORDER BY a.board.modifiedAt")
    List<Auth> findBySellerId(Long sellerid);

    @Query("select a.board from Auth a where a.id=:authid")
    Board findBoardByAuthId(Long authid);

    @Modifying
    @Transactional
    @Query("delete from Board b where b.id=:boardid")
    void deleteByBoardId(Long boardid);
}
