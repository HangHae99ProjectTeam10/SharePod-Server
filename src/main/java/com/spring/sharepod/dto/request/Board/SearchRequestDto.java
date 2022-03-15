package com.spring.sharepod.dto.request.Board;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SearchRequestDto {
    private String filtertype;
    private String searchtitle;
    private String mapdata;
}
