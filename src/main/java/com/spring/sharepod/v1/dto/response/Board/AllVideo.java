package com.spring.sharepod.v1.dto.response.Board;

import com.spring.sharepod.v1.dto.response.VideoAllResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllVideo {
    private String result;
    private String msg;
    private List<VideoAllResponseDto> videoData;
}
