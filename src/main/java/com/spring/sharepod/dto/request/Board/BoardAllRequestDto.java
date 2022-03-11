package com.spring.sharepod.dto.request.Board;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardAllRequestDto {
    private String filtering;
    private String category;
    private String mapdata;
}
