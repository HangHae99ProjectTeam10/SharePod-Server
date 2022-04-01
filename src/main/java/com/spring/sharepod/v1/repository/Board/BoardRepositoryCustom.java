package com.spring.sharepod.v1.repository.Board;

import com.spring.sharepod.v1.dto.response.*;
import com.spring.sharepod.v1.dto.response.Board.BoardAllResponseDto;
import com.spring.sharepod.v1.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.v1.dto.response.User.UserReservation;
import com.spring.sharepod.v1.repository.SearchForm;

import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardAllResponseDto> searchFormQuality(SearchForm seachForm);

    List<BoardAllResponseDto> searchFormCost(SearchForm searchForm);

    List<BoardAllResponseDto> searchFormRecent(SearchForm searchForm);

    List<BoardAllResponseDto> searchAllBoard();

    List<MyBoardResponseDto> getMyBoard(Long userId);

    List<RentBuyer> getRentBuyer(Long userId);

    List<RentSeller> getRentSeller(Long userId);

    List<UserReservation> getReservation(Long userId);

    List<VideoAllResponseDto> videoAll();

//    List<VideoAllResponseDto> videoAll();
}
