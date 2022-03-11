package com.spring.sharepod.controller;


import com.spring.sharepod.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class NoticeRestController {
    private final NoticeService noticeService;





}
