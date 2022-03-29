package com.spring.sharepod.v1.dto.response.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthDataResponseDto {
    @JsonProperty(value = "authImgId")
    private Long id;

    @JsonProperty(value = "authImgUrl")
    private String authImgUrl;

    @JsonProperty(value = "authImgCheck")
    private boolean checkImgBox;
}
