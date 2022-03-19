package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.Reservation.ReservationAcceptNotDTO;
import com.spring.sharepod.dto.request.Reservation.ReservationRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Reservation.ReservationGetDTO;
import com.spring.sharepod.dto.response.Reservation.ReservationGetFinalDTO;
import com.spring.sharepod.entity.*;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.repository.*;
import com.spring.sharepod.validator.ReservationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final AuthRepository authRepository;
    private final AuthimgboxRepository authimgboxRepository;
    private final ReservationValidator reservationValidator;
    //거래 요청
    @Transactional
    public BasicResponseDTO reserRequestService(Long boardid, ReservationRequestDTO requestDTO) {
        //거래요청 validator
        reservationValidator.validateReservationRequest(boardid,requestDTO);

        //게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND));
        //유저 userid로 검색해 가져오기
        User buyer = userRepository.findById(requestDTO.getUserid()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));

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
                .noticeinfo("거래 요청을 하였습니다.")
                .build()).getId();


        return BasicResponseDTO.builder()
                .result("success")
                .msg("거래 요청완료")
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

        //ReservationGetDTO에 데이터 담아주기
        for (int i = 0; i < user.getReservation().size(); i++) {
            reservationGetDTOList.add(ReservationGetDTO.builder()
                    .nickname(user.getReservation().get(i).getBuyer().getNickname())
                    .rentalstart(user.getReservation().get(i).getRentalstart())
                    .rentalend(user.getReservation().get(i).getRentalend())
                    .boardid(user.getReservation().get(i).getBoard().getId())
                    .boardtitle(user.getReservation().get(i).getBoard().getTitle())
                    .build());
        }

        return ReservationGetFinalDTO.builder()
                .result("success")
                .reservation(reservationGetDTOList)
                .build();
    }

    //거래요청 수락/거절
    @Transactional
    public BasicResponseDTO resResponseService(Long boardid, ReservationAcceptNotDTO acceptNotDTO) throws ParseException {
        //buyer
        User buyer = userRepository.findByNickname(acceptNotDTO.getNickname()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));
        //seller
        User seller = userRepository.findById(acceptNotDTO.getSeller()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));
        Board board = boardRepository.findById(boardid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );

        Reservation reservation = reservationRepository.findByBuyerAndBoard(buyer, board);
        if (reservation == null) {
            throw new ErrorCodeException(ErrorCode.RESERVATION_NOT_EXIST);
        }

        //거래 거절 시
        if (!acceptNotDTO.isCheck()) {
            //거래 내역 DB에서 삭제
            reservationRepository.deleteById(reservation.getId());

            //거래 거절 알림 보내기 => 알림 추가(ooo님이 거래 요청을 하였습니다)
            noticeRepository.save(Notice.builder()
                    .buyer(buyer)
                    .seller(seller)
                    .noticeinfo("거래 거절을 하였습니다.")
                    .build()).getId();

            return BasicResponseDTO.builder()
                    .result("success")
                    .msg("거래 거절완료")
                    .build();
        }


        //거래 수락시
        //보드 테이블 appear - false변경
        board.setAppear(false);

        //Auth 테이블 만들기
        Long authid = authRepository.save(Auth.builder()
                .authbuyer(buyer)
                .authseller(seller)
                .board(board)
                .checkorder(false)
                .rentalstart(LocalDate.parse(acceptNotDTO.getRentalstart()))
                .rentalend(LocalDate.parse(acceptNotDTO.getRentalend()))
                .build()).getId();
        Auth auth = authRepository.getById(authid);

        //Authimgbox 테이블 날짜 만큼 갯수 만들기
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date startDate = format.parse(acceptNotDTO.getRentalstart());
        Date endDate = format.parse(acceptNotDTO.getRentalend());
        long calDate = endDate.getTime() - startDate.getTime();
        long calDateDays = calDate / (24 * 60 * 60 * 1000);
        calDateDays = Math.abs(calDateDays);

        for (int i = 0; i < calDateDays + 1; i++) {
            authimgboxRepository.save(Authimgbox.builder()
                    .auth(auth)
                    .imgboxcheck(false)
                    .imgurl(null)
                    .build()).getId();
        }


        //거래 내역 DB에서 삭제
        reservationRepository.deleteById(reservation.getId());

        //거래 수락 알림 보내기 => 알림 추가(ooo님이 거래 요청을 하였습니다)
        noticeRepository.save(Notice.builder()
                .buyer(buyer)
                .seller(seller)
                .noticeinfo("거래 수락을 하였습니다.")
                .build()).getId();
        return BasicResponseDTO.builder()
                .result("success")
                .msg("거래 수락완료")
                .build();
    }


}
