package com.spring.sharepod.v1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Notice {
    private Long noticeId;
    private String noticeName;
    private String userRegion;
    private String otherUserImg;
    private String noticeMsg;
    private Long boardId;
    private Boolean isChat;
}
