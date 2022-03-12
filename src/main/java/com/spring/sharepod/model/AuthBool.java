package com.spring.sharepod.model;

import com.spring.sharepod.dto.response.Board.BoardDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthBool {
    private String result;
    private String msg;
}
