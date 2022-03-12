package com.spring.sharepod.dto.response.Auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDataResponseDTO {
    private Long authimgboxid;
    private String imgurl;
    private boolean imgboxcheck;
}
