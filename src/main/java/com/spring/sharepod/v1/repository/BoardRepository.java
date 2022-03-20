package com.spring.sharepod.v1.repository;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.v1.dto.response.BoardResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {
    //메인 페이지 조회시 limit 갯수로 제한하여 전송함
    @Query(nativeQuery = true, value = "select * from board b where b.main_appear = true ORDER BY b.modified_at asc LIMIT 8;")
    List<Board> findAllByOrderByModifiedAtDesc();

    // 릴스를 limit 갯수만큼 랜덤으로 반환
    @Query(nativeQuery = true, value = "select * from board b ORDER BY RAND() LIMIT :startCount, 3;")
    List<Board> findAllByVideoUrlRan(Long startCount);


    //mapdata,category를 받아서 (appear) 보여주는 정보만 보여주면서 정렬한다. 기준 cost,quility,recent
    @Query(nativeQuery = true, value = "select * from board b where b.board_region=:boardRegion and b.category=:category and b.main_appear = true order by b.boardquility asc LIMIT :startCount ,16;")
    List<Board> findByAndMapAndCategoryByQuility(String boardRegion, String category,Long startCount);

    @Query(nativeQuery = true, value = "select * from board b where b.board_region=:boardRegion and b.category=:category and b.main_appear = true order by b.dailyrentalfee desc LIMIT :startCount ,16;")
    List<Board> findByAndMapAndCategoryByCost(String boardRegion, String category,Long startCount);

    @Query(nativeQuery = true, value = "select *  from board b where b.board_region=:boardRegion and b.category=:category and b.main_appear = true order by b.modified_at desc LIMIT :startCount ,16;")
    List<Board> findByAndMapAndCategoryByCreatedAt(String boardRegion, String category,Long startCount);



    //mapdata,검색(searchtitle)를 받아서 (appear) 보여주는 정보만 보여주면서 정렬한다. 기준 cost,quility,recent
    @Query(nativeQuery = true, value = "select *  from board b where b.board_region=:boardRegion and b.main_appear = true and b.title like %:searchtitle% order by b.boardquility asc LIMIT :startCount, 16;")
    List<Board> findByAndMapAndSearchByQuility(String boardRegion, @Param("searchtitle")String searchtitle,Long startCount);

    @Query(nativeQuery = true, value = "select *  from board b where b.board_region=:boardRegion and b.main_appear = true and b.title like %:searchtitle% order by b.dailyrentalfee desc LIMIT :startCount, 16;")
    List<Board> findByAndMapAndSearchByCost(String boardRegion, @Param("searchtitle")String searchtitle, Long startCount);

    @Query(nativeQuery = true, value = "select *  from board b where b.board_region=:boardRegion and b.main_appear = true and b.title like %:searchtitle% order by b.modified_at desc LIMIT :startCount,16;")
    List<Board> findByAndMapAndSearchByCreatedAt(String boardRegion, @Param("searchtitle") String searchtitle, Long startCount);


    //마이페이지 등록한 목록을 보기 위한 쿼리
    @Query(nativeQuery = true, value = "select * from board b where b.userid=:userId")
    List<Board> findListBoardByUserId(Long userId);
}
