package com.spring.sharepod.v1.dto.response.User;

import com.spring.sharepod.v1.dto.response.RentBuyer;
import com.spring.sharepod.v1.dto.response.RentSeller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserOrderResponseDto {
    private String result;
    private String msg;
    private List<RentBuyer> rentBuyerList;
    private List<RentSeller> rentSellerList;
    private List<UserReservation> userReservationList;

}