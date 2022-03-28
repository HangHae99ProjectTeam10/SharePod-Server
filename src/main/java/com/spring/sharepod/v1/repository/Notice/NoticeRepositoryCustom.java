package com.spring.sharepod.v1.repository.Notice;

import com.spring.sharepod.v1.dto.response.Notice;

import java.util.List;

public interface NoticeRepositoryCustom {
    List<Notice> noticeInfoList(Long userId);
}
