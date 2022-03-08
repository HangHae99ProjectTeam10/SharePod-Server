package com.spring.sharepod.repository;

import com.spring.sharepod.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long> {
}
