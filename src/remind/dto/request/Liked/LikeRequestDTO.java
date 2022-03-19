package com.spring.sharepod.dto.request.Liked;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeRequestDTO {
    private Long userid;
    private LocalDateTime rentalstart;
    private LocalDateTime rentalend;
}
