package com.spring.sharepod.v1.repository.Board;

import com.spring.sharepod.v1.dto.response.BoardAllResponseDto;
import com.spring.sharepod.v1.dto.response.MyBoardResponseDto;
import com.spring.sharepod.v1.dto.response.RentBuyer;
import com.spring.sharepod.v1.dto.response.RentSeller;
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
}
