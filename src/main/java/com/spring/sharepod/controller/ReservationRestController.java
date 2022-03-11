package com.spring.sharepod.controller;

import com.spring.sharepod.dto.request.Reservation.ReservationAcceptNotDTO;
import com.spring.sharepod.dto.request.Reservation.ReservationRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Reservation.ReservationGetFinalDTO;
import com.spring.sharepod.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class ReservationRestController {
    private final ReservationService reservationService;

    //거래 요청
    @PostMapping("/reservation/request/{boardid}")
    public BasicResponseDTO reservationRequest(@PathVariable Long boardid, @RequestBody ReservationRequestDTO requestDTO){
        return reservationService.reserRequestService(boardid,requestDTO);
    }

    //거래 요청 목록(현재 접속한 사람에게 온 요청 목록)
    @GetMapping("/reservation/{userid}")
    public ReservationGetFinalDTO reservationGetControll(@PathVariable Long userid){
        return reservationService.reservationGetService(userid);
    }

    //거래 요청 수락/거절
    @PostMapping("/reservation/response/{boardid}")
    public BasicResponseDTO resResponse(@PathVariable Long boardid, @RequestBody ReservationAcceptNotDTO reservationAcceptNotDTO) throws ParseException {
        return reservationService.resResponseService(boardid,reservationAcceptNotDTO);

    }
}