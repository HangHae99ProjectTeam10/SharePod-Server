package com.spring.sharepod.v1.repository.Notice;

import com.spring.sharepod.v1.dto.response.notice.Notice;

import java.util.List;

public interface NoticeRepositoryCustom {
    //24번 API 알림 갯수, 25번 API 알림 목록 띄우기
    List<Notice> noticeInfoList(Long userId);
}
