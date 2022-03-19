package com.spring.sharepod.model;

import com.spring.sharepod.v1.dto.response.BoardResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardDetail {
    private String result;
    private String msg;
    private BoardResponseDto.BoardDetail data;
}
