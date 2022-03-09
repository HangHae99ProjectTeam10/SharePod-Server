package com.spring.sharepod.repository;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    @Query("select c from Liked c where exists (select c from c where c.board.id=:boardid and c.user.id=:userid)")
    Boolean findByLiked(Long boardid, Long userid);

    Liked findByUserAndBoard(User user, Board board);
}
