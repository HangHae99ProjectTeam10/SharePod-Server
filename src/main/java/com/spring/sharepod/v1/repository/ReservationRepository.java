package com.spring.sharepod.v1.repository;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Reservation;
import com.spring.sharepod.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByBuyerAndBoard(User buyer, Board board);

    void deleteAllByBoard(Board board);

    List<Reservation> findAllByBoard(Board board);
}
