package com.spring.sharepod.controller;

import com.spring.sharepod.dto.request.ReservationRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
