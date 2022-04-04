package com.spring.sharepod.v1.service;

import com.spring.sharepod.entity.*;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.dto.request.ReservationRequestDto;
import com.spring.sharepod.v1.dto.response.Reservation.ReservationGetDTO;
import com.spring.sharepod.v1.dto.response.Reservation.ReservationNoticeList;
import com.spring.sharepod.v1.dto.response.Reservation.ReservationResponseDto;
import com.spring.sharepod.v1.repository.Auth.AuthRepository;
import com.spring.sharepod.v1.repository.AuthImgRepository;
import com.spring.sharepod.v1.repository.Board.BoardRepository;
import com.spring.sharepod.v1.repository.Notice.NoticeRepository;
import com.spring.sharepod.v1.repository.Reservation.ReservationRepository;
import com.spring.sharepod.v1.repository.UserRepository;
import com.spring.sharepod.v1.validator.ReservationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    private final AuthImgRepository authImgRepository;
    private final ReservationValidator reservationValidator;

    // 17번 API거래 요청
    @Transactional
    public ReservationResponseDto.ReservationDeal reserRequestService(Long boardId, ReservationRequestDto.Reservation requestDTO) {
        //거래요청 validator
        reservationValidator.validateReservationRequest(boardId, requestDTO);

        //게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND));
        //유저 userid로 검색해 가져오기
        User buyer = userRepository.findById(requestDTO.getUserId()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));

        //거래 요청 추가
        reservationRepository.save(Reservation.builder()
                .buyer(buyer)
                .seller(board.getUser())
                .board(board)
                .startRental(LocalDate.parse(requestDTO.getStartRental()))
                .endRental(LocalDate.parse(requestDTO.getEndRental()))
                .build());

        char quotes = '"';
        //알림 추가(ooo님이 거래 요청을 하였습니다)
        noticeRepository.save(Notice.builder()
                .receiver(board.getUser())
                .sender(buyer)
                .board(board)
                .noticeInfo(quotes + board.getTitle() + quotes + "  거래 요청을 하였습니다.")
                .isChat(false)
                .build());

        return ReservationResponseDto.ReservationDeal.builder()
                .result("success")
                .msg("거래 요청완료")
                .userId(requestDTO.getUserId())
                .boardId(boardId)
                .startRental(requestDTO.getStartRental())
                .endRental(requestDTO.getEndRental())
                .build();
    }

    //18번 API 거래 요청 목록(현재 접속한 사람에게 온 요청 목록)
    @Transactional
    public ReservationResponseDto.ReservationGetFinalDTO reservationGetService(Long sellerId) {
        List<ReservationGetDTO> querydslReservationList = reservationRepository.reservationList(sellerId);

        return ReservationResponseDto.ReservationGetFinalDTO.builder()
                .result("suceess")
                .msg("거래 목록 리스트 반환")
                .reservationList(querydslReservationList)
                .build();
    }

    //19번 API 거래 요청 수락/거절
    @Transactional
    public ReservationResponseDto.accReservationDTO resResponseService(Long boardId, ReservationRequestDto.AcceptOrNot acceptNotDTO) throws ParseException {
        //buyer
        User buyer = userRepository.findByNickName(acceptNotDTO.getBuyerNickName()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));
        //seller
        User seller = userRepository.findById(acceptNotDTO.getSellerId()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );

        Reservation reservation = reservationRepository.findByBuyerAndBoard(buyer, board);
        Long reservationId = reservation.getId();
        if (reservation == null) {
            throw new ErrorCodeException(ErrorCode.RESERVATION_NOT_EXIST);
        }

        //거래 거절 시
        if (!acceptNotDTO.isCheck()) {
            //거래 내역 DB에서 삭제
            reservationRepository.deleteById(reservation.getId());

            char quotes = '"';
            //거래 거절 알림 보내기 => 알림 추가(ooo님이 거래 거절을 하였습니다)
            noticeRepository.save(Notice.builder()
                    .board(board)
                    .receiver(buyer)
                    .sender(seller)
                    .noticeInfo(quotes + board.getTitle() + quotes + "  거래 거절을 하였습니다.")
                    .isChat(false)
                    .build());

            return ReservationResponseDto.accReservationDTO.builder()
                    .result("success")
                    .msg("거래 거절완료")
                    .boardId(boardId)
                    .sellerId(acceptNotDTO.getSellerId())
                    .buyerNickName(acceptNotDTO.getBuyerNickName())
                    .check(acceptNotDTO.isCheck())
                    .build();
        }

        //거래 수락시
        //보드 테이블 appear - false변경
        board.setAppear(false);

        //Auth 테이블 만들기
        Long authid = authRepository.save(Auth.builder()
                .authBuyer(buyer)
                .authSeller(seller)
                .board(board)
                .sellectAllImg(false)
                .startRental(reservation.getStartRental())
                .endRental(reservation.getEndRental())
                .build()).getId();

        Auth auth = authRepository.getById(authid);

        //Authimgbox 테이블 날짜 만큼 갯수 만들기
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date startDate = format.parse(reservation.getStartRental().toString());
        Date endDate = format.parse(reservation.getEndRental().toString());
        long calDate = endDate.getTime() - startDate.getTime();
        long calDateDays = calDate / (24 * 60 * 60 * 1000);
        calDateDays = Math.abs(calDateDays);

        for (int i = 0; i < calDateDays + 1; i++) {
            authImgRepository.save(AuthImg.builder()
                    .authImgUrl(null)
                    .checkImgBox(false)
                    .auth(auth)
                    .build());
        }

        //첫번째로 거래를 수락한 사람의 Reservation DB행 지우기
        reservationRepository.deleteById(reservation.getId());
        char quotes = '"';
        //거래 수락 알림 보내기
        noticeRepository.save(Notice.builder()
                .board(board)
                .receiver(buyer)
                .sender(seller)
                .noticeInfo(quotes + board.getTitle() + quotes + "  의 거래를 수락을 하였습니다.")
                .isChat(false)
                .build());

        List<ReservationNoticeList> reservationList = reservationRepository.reservationNoticeList(reservationId);
        for (ReservationNoticeList reservationNoticeList : reservationList) {
            //나머지 거절된 거래들에 대해서 알림 보내기 => 알림 추가("어떤어떤 제목"의 거래가 이미 대여되어 거절 되었습니다)
            noticeRepository.save(Notice.builder()
                    .board(board)
                    .receiver(reservationNoticeList.getBuyer())
                    .sender(reservationNoticeList.getSeller())
                    .noticeInfo(quotes + board.getTitle() + quotes + "의 거래가 이미 대여되어 거절되었습니다.")
                    .build());
        }
        //거래 내역 DB에서 삭제
        reservationRepository.deleteAllByBoard(board);

        return ReservationResponseDto.accReservationDTO.builder()
                .result("success")
                .msg("거래 수락완료")
                .boardId(boardId)
                .sellerId(acceptNotDTO.getSellerId())
                .buyerNickName(acceptNotDTO.getBuyerNickName())
                .check(acceptNotDTO.isCheck())
                .build();
    }
}
