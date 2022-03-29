package com.spring.sharepod.v1.dto.response.Board;

import com.spring.sharepod.v1.dto.response.Board.BoardResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BoardDetail {
    private String result;
    private String msg;
    private BoardResponseDto.BoardDetail data;
}
