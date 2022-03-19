package com.spring.sharepod.v1.validator;


import com.spring.sharepod.entity.Notice;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

import static com.spring.sharepod.exception.CommonError.ErrorCode.*;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class NoticeValidator {
    private final NoticeRepository noticeRepository;


    //알림 목록 valid
    public List<Notice> ValidnoticeList(Long userid) {
        //userid로 notice를 찾는다
        List<Notice> noticeList = noticeRepository.findByBuyerOrSellerId(userid);

        //알림이 하나도 없다면 메시지를 중간에 호출한다.
        if (noticeList == null) {
            throw new ErrorCodeException(NOTICELIST_NOT_EXIST);
        }
        return noticeList;
    }

    //알림 삭제 valid
    public Notice ValidDeleteNotice(Long noticeid) {
        // notice id가 존재하지 않을 시, error 메시지 호출
        Notice findNoticeId = noticeRepository.findById(noticeid).orElseThrow(
                () -> new ErrorCodeException(NOTICE_NOT_EXIST));


        //토큰과 notice의 seller,buyer id와 일치하지 않는다면 error 메시지 호출 이거 해야함
//        if(findNoticeId.getBuyer().getId() != user.getid() && findNoticeId.getSeller().getId() != user.getid()){
//            throw new ErrorCodeException(NOTICE_DELETE_NOT_EXIST);
//        }

        return findNoticeId;
    }

}

