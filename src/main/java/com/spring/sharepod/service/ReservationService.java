package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.Reservation.ReservationRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.entity.*;
import com.spring.sharepod.exception.ErrorCode;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.NoticeRepository;
import com.spring.sharepod.repository.ReservationRepository;
import com.spring.sharepod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;

    @Transactional
    public BasicResponseDTO reserRequestService(Long boardid, ReservationRequestDTO requestDTO) {
        //게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );
        //유저 userid로 검색해 가져오기
        User user = userRepository.findById(requestDTO.getUserid()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND)
        );
        //해당 reservation 가져오기
        Reservation reservation = reservationRepository.findByUserAndBoard(user, board);

        //이미 거래 요청 DB에 존재하면 생성 거절
        if (reservation != null) {
            throw new ErrorCodeException(ErrorCode.RESERVATION_NOT_EXIST);
        }

        //거래 요청 추가
        reservationRepository.save(Reservation.builder()
                .user(user)
                .board(board)
                .resercheck(false)
                .rentalstart(LocalDate.parse(requestDTO.getRentalstart()))
                .rentalend(LocalDate.parse(requestDTO.getRentalend()))
                .build()).getId();

        //알림 추가(ooo님이 거래 요청을 하였습니다)
        noticeRepository.save(Notice.builder()
                .buyer(user)
                .seller(board.getUser())
                .noticetype("거래 요청을 하였습니다.")
                .build()).getId();


        return BasicResponseDTO.builder()
                .result("success")
                .msg("거래 요청 완료")
                .build();
    }

}
