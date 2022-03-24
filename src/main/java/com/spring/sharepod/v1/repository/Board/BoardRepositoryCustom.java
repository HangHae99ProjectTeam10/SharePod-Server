package com.spring.sharepod.v1.repository.Board;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.v1.dto.response.BoardAllResponseDto;
import com.spring.sharepod.v1.repository.SearchForm;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    List<BoardAllResponseDto> searchFormQuality(SearchForm seachForm);

    List<BoardAllResponseDto> searchFormCost(SearchForm searchForm);

    List<BoardAllResponseDto> searchFormRecent(SearchForm searchForm);

    List<BoardAllResponseDto> searchAllBoard();
}
