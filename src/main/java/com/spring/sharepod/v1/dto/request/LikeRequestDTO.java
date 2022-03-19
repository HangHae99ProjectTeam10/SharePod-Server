package com.spring.sharepod.v1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class LikeRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Liked {
        private Long userId;
        private LocalDateTime startRental;
        private LocalDateTime endRental;
    }
}
