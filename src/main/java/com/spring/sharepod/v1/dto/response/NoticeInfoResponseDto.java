package com.spring.sharepod.v1.dto.response;

import com.spring.sharepod.v1.dto.response.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoticeInfoResponseDto {
    private String result;
    private String msg;
    private List<Notice> noticeList;

}
