package com.spring.sharepod.controller;

import com.spring.sharepod.dto.request.Reservation.ReservationRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Reservation.ReservationGetFinalDTO;
import com.spring.sharepod.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class ReservationRestController {
    private final ReservationService reservationService;

    //거래 요청
    @PostMapping("/reservation/request/{boardid}")
    public BasicResponseDTO reservationRequest(@PathVariable Long boardid, @RequestBody ReservationRequestDTO requestDTO){
        return reservationService.reserRequestService(boardid,requestDTO);
    }


}
