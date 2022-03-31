package com.spring.sharepod.v1.validator;


import com.spring.sharepod.entity.Notice;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.repository.Notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.spring.sharepod.exception.CommonError.ErrorCode.NOTICE_NOT_EXIST;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class NoticeValidator {
    private final NoticeRepository noticeRepository;

    //알림 목록 valid
    public int ValidnoticeList(Long userid) {
        //userid로 notice를 찾는다
        int noticeExist = noticeRepository.findByRecieverId(userid);
        return noticeExist;
    }

    //알림 삭제 valid
    public Notice ValidDeleteNotice(Long noticeid) {
        // notice id가 존재하지 않을 시, error 메시지 호출
        Notice findNoticeId = noticeRepository.findById(noticeid).orElseThrow(
                () -> new ErrorCodeException(NOTICE_NOT_EXIST));
        return findNoticeId;
    }
}

