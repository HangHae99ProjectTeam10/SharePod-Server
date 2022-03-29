package com.spring.sharepod.v1.dto.response;

import com.spring.sharepod.v1.dto.response.Board.BoardAllResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardList {
    private String result;
    private String msg;
    private List<BoardAllResponseDto> listdata;
}
