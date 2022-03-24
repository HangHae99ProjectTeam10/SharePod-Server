package com.spring.sharepod.v1.repository;

import com.querydsl.core.types.dsl.NumberPath;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface LikedRepository extends JpaRepository<Liked, Long> {

    @Query("select c from Liked c where c.user=:user and c.board=:board")
    Liked findByUserAndBoard(User user, Board board);

    //userid와 boardid를 통한
    @Query(nativeQuery = true, value = "select exists (SELECT c.id From liked c where c.userid=:userId and c.boardid=:boardId)")
    int existLiked(Long userId, Long boardId);

    @Query("select c from Liked c where c.user.id =:userid")
    List<Liked> findByUserId(Long userid);

}
