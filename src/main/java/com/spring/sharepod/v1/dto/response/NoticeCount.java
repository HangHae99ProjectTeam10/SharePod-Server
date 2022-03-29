package com.spring.sharepod.v1.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoticeCount {
    private String result;
    private String msg;
    private int noticeCnt;
}
