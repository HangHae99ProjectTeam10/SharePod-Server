package com.spring.sharepod.service;


import com.spring.sharepod.dto.response.Notice.NoticeResponseDto;
import com.spring.sharepod.entity.Notice;
import com.spring.sharepod.repository.NoticeRepository;
import com.spring.sharepod.validator.NoticeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeValidator noticeValidator;

    //알림 갯수
    @Transactional
    public int getNoticeCount(Long userid) {
        // user의 정보가 없을 경우에는 count를 0으로 보내준다.
        return noticeRepository.findByCOUNTBuyerOrSellerId(userid);
    }

    //알림 목록 띄우기
    @Transactional
    public List<NoticeResponseDto> getNoticeList(Long userid) {
        //해당하는 user의 알림에 대하여 둘 다 해당하는 모든 List<Notice>를 뽑아내고 그의 noticetype(명칭)에 따라 구분한다.
        List<NoticeResponseDto> noticeResponseDtoList = new ArrayList<>();

        // userid에 의한 모든 경우 알림이 없다면 알림이 존재하지 않는다는 메시지를 호출한다.
        List<Notice> noticeList = noticeValidator.ValidnoticeList(userid);

        for (int i = 0; i < noticeList.size(); i++) {
            //거래 요청의 경우 buyer의 nickname이 필요하다.
            if (Objects.equals(noticeList.get(i).getNoticeinfo(), "거래 요청을 하였습니다.")) {
                NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder()
                        .noticeid(noticeList.get(i).getId())
                        .noticename(noticeList.get(i).getBuyer().getNickname())
                        .noticemsg(noticeList.get(i).getNoticeinfo())
                        .build();
                noticeResponseDtoList.add(noticeResponseDto);
            }
            //거래 거절의 경우 seller의 nickname이 필요하다.
            if (Objects.equals(noticeList.get(i).getNoticeinfo(), "거래 거절을 하였습니다.")) {
                NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder()
                        .noticeid(noticeList.get(i).getId())
                        .noticename(noticeList.get(i).getSeller().getNickname())
                        .noticemsg(noticeList.get(i).getNoticeinfo())
                        .build();
                noticeResponseDtoList.add(noticeResponseDto);
            }
            //거래 수락의 경우 seller의 nickname이 필요하다.
            if (Objects.equals(noticeList.get(i).getNoticeinfo(), "거래 수락을 하였습니다.")) {
                NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder()
                        .noticeid(noticeList.get(i).getId())
                        .noticename(noticeList.get(i).getSeller().getNickname())
                        .noticemsg(noticeList.get(i).getNoticeinfo())
                        .build();
                noticeResponseDtoList.add(noticeResponseDto);
            }
        }
        return noticeResponseDtoList;
    }

    //알림 확인 삭제
    @Transactional
    public void DeleteNotice(Long noticeid, Long userid) {
        //알림이 존재하지 않을 시 에러 메시지 호출
        Notice findNoticeId = noticeValidator.ValidDeleteNotice(noticeid);



        //findNoticeId를 통해 token으로 받은 정보와 일치하는지 확인한다.
         if(Objects.equals(userid, findNoticeId.getSeller().getId()) || Objects.equals(userid, findNoticeId.getBuyer().getId())){

         }

        //존재할 시 해당 id로 알림 삭제
        noticeRepository.deleteByNoticeId(noticeid);
    }

}
