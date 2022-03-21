package com.spring.sharepod.v1.repository.Board;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.v1.repository.SearchForm;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> searchFormQuality(SearchForm seachForm);

    List<Board> searchFormCost(SearchForm searchForm);

    List<Board> searchFormRecent(SearchForm searchForm);
}
