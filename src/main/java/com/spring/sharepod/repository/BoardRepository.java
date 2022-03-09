package com.spring.sharepod.repository;

import com.spring.sharepod.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b where b.appear = true order by b.createdAt asc")
    List<Board> findAllByOrderByCreatedAtDesc();


    @Query("select b from Board b ORDER BY b.dailyrentalfee")
    List<Board> findAllByVideoUrlRan();


    @Query("select b from Board b where b.mapdata=:mapdata and b.category=:category and b.appear = true order by b.boardquility asc")
    List<Board> findByAndMapAndCategoryByQuility(String mapdata, String category);

    @Query("select b from Board b where b.mapdata=:mapdata and b.category=:category and b.appear = true order by b.dailyrentalfee desc")
    List<Board> findByAndMapAndCategoryByCost(String mapdata, String category);

    @Query("select b from Board b where b.mapdata=:mapdata and b.category=:category and b.appear = true order by b.createdAt desc")
    List<Board> findByAndMapAndCategoryByCreatedAt(String mapdata, String category);


    @Query("select b from Board b where b.mapdata=:mapdata and b.appear = true and b.title like %:searchtitle% order by b.boardquility asc")
    List<Board> findByAndMapAndSearchByQuility(String mapdata, @Param("searchtitle")String searchtitle);

    @Query("select b from Board b where b.mapdata=:mapdata and b.appear = true and b.title like %:searchtitle% order by b.dailyrentalfee desc")
    List<Board> findByAndMapAndSearchByCost(String mapdata, @Param("searchtitle")String searchtitle);

    @Query("select b from Board b where b.mapdata=:mapdata and b.appear = true and b.title like %:searchtitle% order by b.createdAt desc")
    List<Board> findByAndMapAndSearchByCreatedAt(String mapdata, @Param("searchtitle")String searchtitle);


}
