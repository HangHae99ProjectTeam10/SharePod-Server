package com.spring.sharepod.dto.request.User;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModifyRequestDTO {
    private String username;
    private String nickname;
    private String mapdata;
    private String userimg;
}
