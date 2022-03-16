package com.spring.sharepod.model;

import com.spring.sharepod.dto.response.Board.*;
import com.spring.sharepod.dto.response.Liked.LikedResponseDto;
import com.spring.sharepod.dto.response.User.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {
    private String result;
    private String msg;
    private UserInfoResponseDto userinfo;
    private List<LikedResponseDto> userlikeboard;
    private List<MyBoardResponseDto> usermyboard;
    private List<RentBuyerResponseDto> rentbuyer;
    private List<RentSellerResponseDto> rentseller;

}