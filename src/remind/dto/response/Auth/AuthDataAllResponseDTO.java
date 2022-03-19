package com.spring.sharepod.dto.response.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDataAllResponseDTO {
    private String result;
    private String msg;
    private Long seller;
    private Long buyer;
    private boolean authcheck;
    private List<AuthDataResponseDTO> data;
    private LocalDate rentalstart;
    private LocalDate rentalend;

}
