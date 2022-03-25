package com.spring.sharepod.v1.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Testtree {
    private String board_region;
    private String board_tag;
    private String category;
}
