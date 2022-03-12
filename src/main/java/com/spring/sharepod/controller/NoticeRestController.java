package com.spring.sharepod.controller;



import com.spring.sharepod.dto.response.Notice.NoticeResponseDto;
import com.spring.sharepod.entity.Notice;
import com.spring.sharepod.model.NoticeCount;
import com.spring.sharepod.model.NoticeInfo;
import com.spring.sharepod.model.Success;
import com.spring.sharepod.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class NoticeRestController {
    private final NoticeService noticeService;

    //알림 갯수 API
    @GetMapping("/notice/count/{userid}")
    public ResponseEntity<NoticeCount> NoticeCount(@PathVariable Long userid){
        int NoticeCount = noticeService.getNoticeCount(userid);
        return new ResponseEntity<>(new NoticeCount("success", "알림 개수 전송 성공", NoticeCount), HttpStatus.OK);
    }
    //알림 목록 띄우기
    @GetMapping("/notice/{userid}")
    public ResponseEntity<NoticeInfo> NoticeList(@PathVariable Long userid){
        List<NoticeResponseDto> noticeList = noticeService.getNoticeList(userid);
        return new ResponseEntity<>(new NoticeInfo("success", "알림 목록 데이터 전송 성공",noticeList), HttpStatus.OK);
    }

    //알림 확인 or 삭제
    @DeleteMapping("/notice/{noticeid}")
    public ResponseEntity<Success> NoticeDelete(@PathVariable Long noticeid){
        noticeService.DeleteNotice(noticeid);
        return new ResponseEntity<>(new Success("success", noticeid + "번 알림 목록 삭제 완료"),HttpStatus.OK);
    }
}
