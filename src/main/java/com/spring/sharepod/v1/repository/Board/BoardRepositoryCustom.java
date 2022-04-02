package com.spring.sharepod.v1.repository.Board;

import com.spring.sharepod.v1.dto.response.*;
import com.spring.sharepod.v1.dto.response.Board.BoardAllResponseDto;
import com.spring.sharepod.v1.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.v1.dto.response.User.UserReservation;
import com.spring.sharepod.v1.repository.SearchForm;

import java.util.List;

public interface BoardRepositoryCustom {
    //14번 상품 카테고리 클릭 시, 상세 리스트 페이지로 이동 - 품질순
    List<BoardAllResponseDto> searchFormQuality(SearchForm seachForm);
    //14번 상품 카테고리 클릭 시, 상세 리스트 페이지로 이동 - 비용순
    List<BoardAllResponseDto> searchFormCost(SearchForm searchForm);
    //14번 상품 카테고리 클릭 시, 상세 리스트 페이지로 이동 - 최신순
    List<BoardAllResponseDto> searchFormRecent(SearchForm searchForm);

    List<BoardAllResponseDto> searchAllBoard();

    //6.2 마이페이지 내가 등록한 글 불러오기
    List<MyBoardResponseDto> getMyBoard(Long userId);

    //6.3 마이페이지 대여한 목록 불러오기
    List<RentBuyer> getRentBuyer(Long userId);

    //6.3 마이페이지 빌려준 목록 불러오기
    List<RentSeller> getRentSeller(Long userId);

    //6.3거래요청 목록 불러오기
    List<UserReservation> getReservation(Long userId);

    //8번 API 릴스 동영상 - 비디오 영상 랜덤 보내주기
    List<VideoAllResponseDto> videoAll();
}
