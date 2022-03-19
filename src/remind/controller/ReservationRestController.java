package com.spring.sharepod.controller;

import com.spring.sharepod.dto.request.Reservation.ReservationAcceptNotDTO;
import com.spring.sharepod.dto.request.Reservation.ReservationRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Reservation.ReservationGetFinalDTO;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.service.ReservationService;
import com.spring.sharepod.validator.TokenValidator;
import jdk.nashorn.internal.parser.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class ReservationRestController {
    private final ReservationService reservationService;
    private final TokenValidator tokenValidator;

    //거래 요청
    @PostMapping("/reservation/request/{boardid}")
    public BasicResponseDTO reservationRequest(@PathVariable Long boardid, @RequestBody ReservationRequestDTO requestDTO, @AuthenticationPrincipal User user){
        tokenValidator.userIdCompareToken(requestDTO.getUserid(), user.getId());
        return reservationService.reserRequestService(boardid,requestDTO);
    }

    //거래 요청 목록(현재 접속한 사람에게 온 요청 목록)
    @GetMapping("/reservation/{userid}")
    public ReservationGetFinalDTO reservationGetControll(@PathVariable Long userid,@AuthenticationPrincipal User user){
        tokenValidator.userIdCompareToken(userid,user.getId());
        return reservationService.reservationGetService(userid);
    }

    //거래 요청 수락/거절
    @PostMapping("/reservation/response/{boardid}")
    public BasicResponseDTO resResponse(@PathVariable Long boardid, @RequestBody ReservationAcceptNotDTO reservationAcceptNotDTO,@AuthenticationPrincipal User user) throws ParseException {
        return reservationService.resResponseService(boardid,reservationAcceptNotDTO);

    }
}