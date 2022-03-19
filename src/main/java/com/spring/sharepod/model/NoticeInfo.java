package com.spring.sharepod.model;

import com.spring.sharepod.v1.dto.response.NoticeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoticeInfo {
    private String result;
    private String msg;
    private List<NoticeResponseDto.Notice> noticeList;

}
