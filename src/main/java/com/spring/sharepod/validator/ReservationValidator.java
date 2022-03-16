package com.spring.sharepod.validator;


import com.spring.sharepod.dto.request.Reservation.ReservationRequestDTO;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Reservation;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.ReservationRepository;
import com.spring.sharepod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class ReservationValidator {
    private final ReservationRepository reservationRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void validateReservationRequest(Long boardid, ReservationRequestDTO requestDTO){
        //게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND));
        //유저 userid로 검색해 가져오기
        User buyer = userRepository.findById(requestDTO.getUserid()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));
        //해당 reservation 가져오기
        Reservation reservation = reservationRepository.findByBuyerAndBoard(buyer, board);

        //이미 거래 요청 DB에 존재하면 생성 거절
        if (reservation != null) {
            throw new ErrorCodeException(ErrorCode.RESERVATION_NOT_EXIST);
        }
    }
}
