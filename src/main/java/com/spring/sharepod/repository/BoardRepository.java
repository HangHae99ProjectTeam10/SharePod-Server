package com.spring.sharepod.repository;

import com.spring.sharepod.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
