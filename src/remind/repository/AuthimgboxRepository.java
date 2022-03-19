package com.spring.sharepod.repository;

import com.spring.sharepod.entity.Authimgbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthimgboxRepository extends JpaRepository<Authimgbox, Long> {

    @Modifying
    @Transactional
    @Query("delete from Authimgbox a where a.auth.id=:authid")
    void deleteByAuthId(Long authid);


    @Query("select a from Authimgbox a where a.auth.authbuyer.id=:buyerid")
    Optional<Authimgbox> findByBuyerId(Long buyerid);

}
