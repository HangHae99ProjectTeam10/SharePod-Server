package com.spring.sharepod.v1.repository;

import com.spring.sharepod.entity.AuthImg;
import com.spring.sharepod.model.AuthImgModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface AuthImgRepository extends JpaRepository<AuthImg, Long> {

    @Modifying
    @Transactional
    @Query("delete from AuthImg a where a.auth.id=:authid")
    void deleteByAuthId(Long authid);


    @Query("select a from AuthImg a where a.auth.authBuyer.id=:buyerId and a.id=:authImgId")
    Optional<AuthImg> findByBuyerAndAuthImgId(Long buyerId, Long authImgId);

}
