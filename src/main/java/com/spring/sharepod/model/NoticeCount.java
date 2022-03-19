package com.spring.sharepod.model;


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
