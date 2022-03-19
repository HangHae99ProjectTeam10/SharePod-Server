package com.spring.sharepod.v1.controller;


import com.spring.sharepod.entity.User;
import com.spring.sharepod.model.NoticeCount;
import com.spring.sharepod.model.NoticeInfo;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.v1.dto.response.NoticeResponseDto;
import com.spring.sharepod.v1.service.NoticeService;
import com.spring.sharepod.v1.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class NoticeRestController {
    private final NoticeService noticeService;
    private final TokenValidator tokenValidator;

    //24번 API **알림 갯수 API (구현 완료)
    @GetMapping("/notice/count/{userId}")
    public ResponseEntity<NoticeCount> NoticeCount(@PathVariable Long userId, @AuthenticationPrincipal User user){
        //userid와 토큰 비교 validator
        tokenValidator.userIdCompareToken(userId,user.getId());

        int NoticeCount = noticeService.getNoticeCount(userId);
        return new ResponseEntity<>(new NoticeCount("success", "알림 개수 전송 성공", NoticeCount), HttpStatus.OK);
    }
    //25번 API **알림 목록 띄우기 (구현 완료)
    @GetMapping("/notice/{userId}")
    public ResponseEntity<NoticeInfo> NoticeList(@PathVariable Long userId, @AuthenticationPrincipal User user){
        //userid와 토큰 비교 validator
        tokenValidator.userIdCompareToken(userId,user.getId());

        List<NoticeResponseDto.Notice> noticeList = noticeService.getNoticeList(userId);
        return new ResponseEntity<>(new NoticeInfo("success", "알림 목록 데이터 전송 성공",noticeList), HttpStatus.OK);
    }

    //26번 API 알림 확인 or 삭제 (구현 완료)
    @DeleteMapping("/notice/{noticeId}")
    public ResponseEntity<Success> NoticeDelete(@PathVariable Long noticeId,@AuthenticationPrincipal User user){

        noticeService.DeleteNotice(noticeId,user.getId());
        return new ResponseEntity<>(new Success("success", noticeId + "번 알림 목록 삭제 완료"),HttpStatus.OK);
    }
}
