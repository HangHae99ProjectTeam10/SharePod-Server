package com.spring.sharepod.v1.controller;

import com.spring.sharepod.entity.User;
import com.spring.sharepod.v1.dto.request.ReservationRequestDto;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.dto.response.ReservationResponseDto;
import com.spring.sharepod.v1.service.ReservationService;
import com.spring.sharepod.v1.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class ReservationRestController {
    private final ReservationService reservationService;
    private final TokenValidator tokenValidator;

    // 17번 API거래 요청
    @PostMapping("/reservation/request/{boardId}")
    public BasicResponseDTO reservationRequest(@PathVariable Long boardId, @RequestBody ReservationRequestDto.Reservation requestDTO, @AuthenticationPrincipal User user){

        tokenValidator.userIdCompareToken(requestDTO.getUserId(), user.getId());
        return reservationService.reserRequestService(boardId,requestDTO);
    }

    //18번 API 거래 요청 목록(현재 접속한 사람에게 온 요청 목록)
    @GetMapping("/reservation/{userId}")
    public ReservationResponseDto.ReservationGetFinalDTO reservationGetControll(@PathVariable Long userId, @AuthenticationPrincipal User user){
        tokenValidator.userIdCompareToken(userId,user.getId());
        return reservationService.reservationGetService(userId);
    }

    //19번 API 거래 요청 수락/거절
    @PostMapping("/reservation/response/{boardId}")
    public BasicResponseDTO resResponse(@PathVariable Long boardId, @RequestBody ReservationRequestDto.AcceptOrNot reservationAcceptNotDTO, @AuthenticationPrincipal User user) throws ParseException {
        tokenValidator.userIdCompareToken(reservationAcceptNotDTO.getSellerId(), user.getId());
        return reservationService.resResponseService(boardId,reservationAcceptNotDTO);

    }
}