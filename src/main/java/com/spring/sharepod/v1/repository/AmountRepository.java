package com.spring.sharepod.v1.repository;

import com.spring.sharepod.entity.Amount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountRepository extends JpaRepository<Amount, Long> {
}
