package com.spring.sharepod.repository;

import com.spring.sharepod.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {
    //메인 페이지 조회시 limit 갯수로 제한하여 전송함
    @Query(nativeQuery = true, value = "select * from board b where b.appear = true ORDER BY b.modified_at asc LIMIT :limitcount")
    List<Board> findAllByOrderByCreatedAtDesc(Long limitcount);

    // 릴스를 limit 갯수만큼 랜덤으로 반환
    @Query(nativeQuery = true, value = "select * from board b ORDER BY RAND() LIMIT :limitcount")
    List<Board> findAllByVideoUrlRan(Long limitcount);


    //mapdata,category를 받아서 (appear) 보여주는 정보만 보여주면서 정렬한다. 기준 cost,quility,recent
    @Query(nativeQuery = true, value = "select * from board b where b.mapdata=:mapdata and b.category=:category and b.appear = true order by b.boardquility asc LIMIT :limitcount")
    List<Board> findByAndMapAndCategoryByQuility(String mapdata, String category,Long limitcount);

    @Query(nativeQuery = true, value = "select * from board b where b.mapdata=:mapdata and b.category=:category and b.appear = true order by b.dailyrentalfee desc LIMIT :limitcount")
    List<Board> findByAndMapAndCategoryByCost(String mapdata, String category,Long limitcount);

    @Query(nativeQuery = true, value = "select *  from board b where b.mapdata=:mapdata and b.category=:category and b.appear = true order by b.modified_at desc LIMIT :limitcount")
    List<Board> findByAndMapAndCategoryByCreatedAt(String mapdata, String category,Long limitcount);



    //mapdata,검색(searchtitle)를 받아서 (appear) 보여주는 정보만 보여주면서 정렬한다. 기준 cost,quility,recent
    @Query(nativeQuery = true, value = "select *  from board b where b.mapdata=:mapdata and b.appear = true and b.title like %:searchtitle% order by b.boardquility asc LIMIT :limitcount")
    List<Board> findByAndMapAndSearchByQuility(String mapdata, @Param("searchtitle")String searchtitle,Long limitcount);

    @Query(nativeQuery = true, value = "select *  from board b where b.mapdata=:mapdata and b.appear = true and b.title like %:searchtitle% order by b.dailyrentalfee desc LIMIT :limitcount")
    List<Board> findByAndMapAndSearchByCost(String mapdata, @Param("searchtitle")String searchtitle, Long limitcount);

    @Query(nativeQuery = true, value = "select *  from board b where b.mapdata=:mapdata and b.appear = true and b.title like %:searchtitle% order by b.modified_at desc LIMIT :limitcount")
    List<Board> findByAndMapAndSearchByCreatedAt(String mapdata, @Param("searchtitle") String searchtitle, Long limitcount);


    //마이페이지 등록한 목록을 보기 위한 쿼리
    @Query("select b from Board b where b.user.id =:userid")
    List<Board> findByUserId(Long userid);
}
