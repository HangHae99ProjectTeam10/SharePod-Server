package com.spring.sharepod.v1.controller;

import com.spring.sharepod.entity.User;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.dto.response.notice.NoticeCount;
import com.spring.sharepod.v1.dto.response.notice.NoticeInfoResponseDto;
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

@RequiredArgsConstructor
@RestController
public class NoticeRestController {
    private final NoticeService noticeService;
    private final TokenValidator tokenValidator;

    //24번 API 알림 갯수
    @GetMapping("/notice/count/{userId}")
    public ResponseEntity<NoticeCount> NoticeCount(@PathVariable Long userId, @AuthenticationPrincipal User user){
        tokenValidator.userIdCompareToken(userId,user.getId());
        int NoticeCount = noticeService.getNoticeCount(userId);
        return new ResponseEntity<>(new NoticeCount("success", "알림 개수 전송 성공", NoticeCount), HttpStatus.OK);
    }

    //25번 API 알림 목록 띄우기
    @GetMapping("/notice/{userId}")
    public NoticeInfoResponseDto NoticeList(@PathVariable Long userId, @AuthenticationPrincipal User user){
        tokenValidator.userIdCompareToken(userId,user.getId());
        return noticeService.getNoticeList(userId);
    }

    //26번 API 알림 확인 or 삭제
    @DeleteMapping("/notice/{noticeId}")
    public BasicResponseDTO NoticeDelete(@PathVariable Long noticeId, @AuthenticationPrincipal User user){
        noticeService.DeleteNotice(noticeId,user.getId());
        return BasicResponseDTO.builder()
                .result("success")
                .msg("알림 삭제 성공")
                .build();
    }
}
