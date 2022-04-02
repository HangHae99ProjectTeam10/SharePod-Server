package com.spring.sharepod.v1.dto.response.Board;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardDetail {
    private String result;
    private String msg;
    private BoardResponseDto.BoardDetail data;
}
