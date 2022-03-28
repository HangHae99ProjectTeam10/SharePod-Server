package com.spring.sharepod.v1.repository.Notice;

import com.spring.sharepod.v1.dto.response.NoticeInfoList;

import java.util.List;

public interface NoticeRepositoryCustom {
    List<NoticeInfoList> noticeInfoList(Long userId);
}
