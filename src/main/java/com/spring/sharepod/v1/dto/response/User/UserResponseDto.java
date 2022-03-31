package com.spring.sharepod.v1.dto.response.User;

import com.spring.sharepod.v1.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.v1.dto.response.Liked.LikedListResponseDto;
import com.spring.sharepod.v1.dto.response.RentBuyer;
import com.spring.sharepod.v1.dto.response.RentSeller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Login {
        private String result;
        private String msg;
        private Long userId;
        private String nickName;
        private String userRegion;
        private String userImg;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class LoginReFreshToken {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserModifiedInfo {
        private String result;
        private String msg;
        private Long userId;
        private String username;
        private String userNickname;
        private String userRegion;
        private String userModifiedImg;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserLikedList {
        private String result;
        private String msg;
        private List<LikedListResponseDto> userLikedBoard;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserMyBoardList {
        private String result;
        private String msg;
        private List<MyBoardResponseDto> userMyBoard;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserMyOrderList {
        private String result;
        private String msg;
        private List<RentBuyer> rentBuyerList;
        private List<RentSeller> rentSellerList;
        private List<UserReservation> userReservationList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserBuyerList {
        private List<RentBuyer> rentBuyerList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSellerList {
        private List<RentSeller> rentSellerList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserReservationList {
        private List<UserReservation> userReservationList;
    }
}
