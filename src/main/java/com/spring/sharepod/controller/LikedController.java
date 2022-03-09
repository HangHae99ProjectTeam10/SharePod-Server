package com.spring.sharepod.controller;

import com.spring.sharepod.dto.response.BoardAllResponseDto;
import com.spring.sharepod.model.BoardList;
import com.spring.sharepod.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikedController {
    private final LikedService likedService;


}
