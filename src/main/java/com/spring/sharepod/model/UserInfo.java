package com.spring.sharepod.model;

import com.spring.sharepod.v1.dto.response.BoardResponseDto;
import com.spring.sharepod.v1.dto.response.LikedResponseDto;
import com.spring.sharepod.v1.dto.response.UserResponseDto;
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
    private UserResponseDto.UserInfo userInfo;
    private List<LikedResponseDto.Liked> userLikedBoard;
    private List<BoardResponseDto.MyBoard> userMyBoard;
    private List<UserResponseDto.RentBuyer> rentBuyList;
    private List<UserResponseDto.RentSeller> rentSellList;

}