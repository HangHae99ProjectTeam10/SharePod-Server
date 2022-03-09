package com.spring.sharepod.model;

import com.spring.sharepod.dto.response.BoardAllResponseDto;
import com.spring.sharepod.dto.response.BoardDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardDetail {
    private String result;
    private String msg;
    private BoardDetailResponseDto data;
}
