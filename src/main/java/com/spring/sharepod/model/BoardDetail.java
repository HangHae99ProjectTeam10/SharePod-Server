package com.spring.sharepod.model;

import com.spring.sharepod.dto.response.Board.BoardDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardDetail {
    private String result;
    private String msg;
    private BoardDetailResponseDto data;
}
