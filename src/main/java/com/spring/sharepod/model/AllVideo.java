package com.spring.sharepod.model;

import com.spring.sharepod.dto.response.Board.VideoAllResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllVideo {
    private String result;
    private String msg;
    private List<VideoAllResponseDto> videodata;
}
