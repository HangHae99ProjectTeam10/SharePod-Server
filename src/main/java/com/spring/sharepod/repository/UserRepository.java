package com.spring.sharepod.repository;

import com.spring.sharepod.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
