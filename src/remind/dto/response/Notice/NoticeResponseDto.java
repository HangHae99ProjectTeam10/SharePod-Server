package com.spring.sharepod.dto.response.Notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NoticeResponseDto {
    private Long noticeid;
    private String noticename;
    private String noticemsg;

}
