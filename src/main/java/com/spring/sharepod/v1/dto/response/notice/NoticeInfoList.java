package com.spring.sharepod.v1.dto.response.notice;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeInfoList {
    private Long id;
    private String nickName;
    private String noticeInfo;
}
