package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.Reservation.ReservationRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Reservation.ReservationGetDTO;
import com.spring.sharepod.dto.response.Reservation.ReservationGetFinalDTO;
import com.spring.sharepod.entity.*;
import com.spring.sharepod.exception.ErrorCode;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.NoticeRepository;
import com.spring.sharepod.repository.ReservationRepository;
import com.spring.sharepod.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;

    //거래 요청
    @Transactional
    public BasicResponseDTO reserRequestService(Long boardid, ReservationRequestDTO requestDTO) {
        //게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );
        //유저 userid로 검색해 가져오기
        User buyer = userRepository.findById(requestDTO.getUserid()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND)
        );
        //해당 reservation 가져오기
        Reservation reservation = reservationRepository.findByBuyerAndBoard(buyer, board);

        //이미 거래 요청 DB에 존재하면 생성 거절
        if (reservation != null) {
            throw new ErrorCodeException(ErrorCode.RESERVATION_NOT_EXIST);
        }

        //거래 요청 추가
        reservationRepository.save(Reservation.builder()
                .buyer(buyer)
                .seller(board.getUser())
                .board(board)
                .rentalstart(LocalDate.parse(requestDTO.getRentalstart()))
                .rentalend(LocalDate.parse(requestDTO.getRentalend()))
                .build()).getId();

        //알림 추가(ooo님이 거래 요청을 하였습니다)
        noticeRepository.save(Notice.builder()
                .buyer(buyer)
                .seller(board.getUser())
                .noticetype("거래 요청을 하였습니다.")
                .build()).getId();


        return BasicResponseDTO.builder()
                .result("success")
                .msg("거래 요청 완료")
                .build();
    }

    //거래요청 목록
    @Transactional
    public ReservationGetFinalDTO reservationGetService(Long seller) {
        //유저 userid로 검색해 유저객체 가져오기
        User user = userRepository.findById(seller).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND)
        );
        List<ReservationGetDTO> reservationGetDTOList = new ArrayList<>();
//        List<Reservation> reservations = user.getReservation();
        //ReservationGetDTO에 데이터 담아주기
        for (int i = 0; i < user.getReservation().size(); i++) {
            reservationGetDTOList.add(ReservationGetDTO.builder()
                    .nickname(user.getReservation().get(i).getBuyer().getNickname())
                    .rentalstart(user.getReservation().get(i).getRentalstart())
                    .rentalend(user.getReservation().get(i).getRentalend())
                    .boardtitle(user.getReservation().get(i).getBoard().getTitle())
                    .build());
        }

        return ReservationGetFinalDTO.builder()
                .result("success")
                .reservation(reservationGetDTOList)
                .build();
    }

}
