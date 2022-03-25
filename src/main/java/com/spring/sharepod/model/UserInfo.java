package com.spring.sharepod.model;

import com.spring.sharepod.v1.dto.response.*;
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
    private UserInfoResponseDto userInfo;
    private List<LikedListResponseDto> userLikedBoard;
    private List<MyBoardResponseDto> userMyBoard;
    private List<RentBuyer> rentBuyList;
    private List<RentSeller> rentSellList;

}