package com.spring.sharepod.v1.dto.response;

import com.spring.sharepod.v1.dto.response.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserOrder {
    private String result;
    private String msg;
    private List<RentBuyer> rentBuyerList;
    private List<RentSeller> rentSellerList;
    private List<UserReservation> userReservationList;

}