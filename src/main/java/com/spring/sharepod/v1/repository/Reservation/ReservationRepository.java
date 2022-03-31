package com.spring.sharepod.v1.repository.Reservation;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Reservation;
import com.spring.sharepod.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
    Reservation findByBuyerAndBoard(User buyer, Board board);

    void deleteAllByBoard(Board board);
}
