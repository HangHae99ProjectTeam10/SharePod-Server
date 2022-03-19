package com.spring.sharepod.v1.service;

import com.spring.sharepod.entity.*;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.dto.request.ReservationRequestDto;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.dto.response.ReservationResponseDto;
import com.spring.sharepod.v1.repository.*;
import com.spring.sharepod.v1.validator.ReservationValidator;
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
    private final AuthImgRepository authImgRepository;
    private final ReservationValidator reservationValidator;

    //17번 거래 요청 (구현 완료)
    @Transactional
    public BasicResponseDTO reserRequestService(Long boardId, ReservationRequestDto.Reservation requestDTO) {
        //거래요청 validator
        reservationValidator.validateReservationRequest(boardId,requestDTO);

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
                .build()).getId();

        //알림 추가(ooo님이 거래 요청을 하였습니다)
        noticeRepository.save(Notice.builder()
                .buyer(buyer)
                .seller(board.getUser())
                .noticeInfo("거래 요청을 하였습니다.")
                .build()).getId();


        return BasicResponseDTO.builder()
                .result("success")
                .msg("거래 요청완료")
                .build();
    }

    //18번 거래요청 목록 (구현 완료)
    @Transactional
    public ReservationResponseDto.ReservationGetFinalDTO reservationGetService(Long seller) {
        //유저 userid로 검색해 유저객체 가져오기
        User user = userRepository.findById(seller).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND)
        );
        List<ReservationResponseDto.ReservationGetDTO> reservationGetDTOList = new ArrayList<>();

        //ReservationGetDTO에 데이터 담아주기
        for (int i = 0; i < user.getReservation().size(); i++) {
            reservationGetDTOList.add(ReservationResponseDto.ReservationGetDTO.builder()
                    .nickName(user.getReservation().get(i).getBuyer().getNickName())
                    .startRental(user.getReservation().get(i).getStartRental())
                    .endRental(user.getReservation().get(i).getEndRental())
                    .boardId(user.getReservation().get(i).getBoard().getId())
                    .boardTitle(user.getReservation().get(i).getBoard().getTitle())
                    .build());
        }

        return ReservationResponseDto.ReservationGetFinalDTO.builder()
                .result("success")
                .reservationList(reservationGetDTOList)
                .build();
    }

    //API 19번 거래요청 수락/거절 (구현 중)
    @Transactional
    public BasicResponseDTO resResponseService(Long boardId, ReservationRequestDto.AcceptOrNot acceptNotDTO) throws ParseException {
        //buyer
        User buyer = userRepository.findByNickName(acceptNotDTO.getBuyerNickname()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));
        //seller
        User seller = userRepository.findById(acceptNotDTO.getSellerId()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));

        Board board = boardRepository.findById(boardId).orElseThrow(
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
                    .noticeInfo("거래 거절을 하였습니다.")
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
                .authBuyer(buyer)
                .authSeller(seller)
                .board(board)
                .sellectAllImg(false)
                .startRental(LocalDate.parse(acceptNotDTO.getStartRental()))
                .endRental(LocalDate.parse(acceptNotDTO.getEndRental()))
                .build()).getId();

        Auth auth = authRepository.getById(authid);

        //Authimgbox 테이블 날짜 만큼 갯수 만들기
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date startDate = format.parse(acceptNotDTO.getStartRental());
        Date endDate = format.parse(acceptNotDTO.getEndRental());
        long calDate = endDate.getTime() - startDate.getTime();
        long calDateDays = calDate / (24 * 60 * 60 * 1000);
        calDateDays = Math.abs(calDateDays);

        for (int i = 0; i < calDateDays + 1; i++) {
            authImgRepository.save(AuthImg.builder()
                            .authImgUrl(null)
                            .checkImgBox(false)
                            .auth(auth)
                            .build()).getId();

        }


        //거래 내역 DB에서 삭제
        reservationRepository.deleteById(reservation.getId());

        //거래 수락 알림 보내기 => 알림 추가(ooo님이 거래 요청을 하였습니다)
        noticeRepository.save(Notice.builder()
                .buyer(buyer)
                .seller(seller)
                .noticeInfo("거래 수락을 하였습니다.")
                .build()).getId();

        return BasicResponseDTO.builder()
                .result("success")
                .msg("거래 수락완료")
                .build();
    }


}
