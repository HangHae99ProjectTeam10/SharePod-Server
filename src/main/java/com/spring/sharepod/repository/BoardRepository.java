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

    @Query(nativeQuery = true, value = "select * from board b where b.appear = true ORDER BY b.modified_at asc LIMIT :limitcount")
    List<Board> findAllByOrderByCreatedAtDesc(int limitcount);

    @Query(nativeQuery = true, value = "select * from board b ORDER BY RAND() LIMIT :limitcount")
    List<Board> findAllByVideoUrlRan(int limitcount);

    @Query(nativeQuery = true, value = "select * from board b where b.mapdata=:mapdata and b.category=:category and b.appear = true order by b.boardquility asc LIMIT :limitcount")
    List<Board> findByAndMapAndCategoryByQuility(String mapdata, String category,int limitcount);

    @Query(nativeQuery = true, value = "select * from board b where b.mapdata=:mapdata and b.category=:category and b.appear = true order by b.dailyrentalfee desc LIMIT :limitcount")
    List<Board> findByAndMapAndCategoryByCost(String mapdata, String category,int limitcount);

    @Query(nativeQuery = true, value = "select *  from board b where b.mapdata=:mapdata and b.category=:category and b.appear = true order by b.modified_at desc LIMIT :limitcount")
    List<Board> findByAndMapAndCategoryByCreatedAt(String mapdata, String category,int limitcount);


    @Query(nativeQuery = true, value = "select *  from board b where b.mapdata=:mapdata and b.appear = true and b.title like %:searchtitle% order by b.boardquility asc LIMIT :limitcount")
    List<Board> findByAndMapAndSearchByQuility(String mapdata, @Param("searchtitle")String searchtitle,int limitcount);

    @Query(nativeQuery = true, value = "select *  from board b where b.mapdata=:mapdata and b.appear = true and b.title like %:searchtitle% order by b.dailyrentalfee desc LIMIT :limitcount")
    List<Board> findByAndMapAndSearchByCost(String mapdata, @Param("searchtitle")String searchtitle, int limitcount);

    @Query(nativeQuery = true, value = "select *  from board b where b.mapdata=:mapdata and b.appear = true and b.title like %:searchtitle% order by b.modified_at desc LIMIT :limitcount")
    List<Board> findByAndMapAndSearchByCreatedAt(String mapdata, @Param("searchtitle") String searchtitle, int limitcount);
}
