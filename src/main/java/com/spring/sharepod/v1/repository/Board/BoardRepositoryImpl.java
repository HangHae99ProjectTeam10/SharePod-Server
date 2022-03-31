package com.spring.sharepod.v1.repository.Board;


import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.sharepod.v1.dto.response.*;
import com.spring.sharepod.v1.dto.response.Board.BoardAllResponseDto;
import com.spring.sharepod.v1.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.v1.dto.response.User.UserReservation;
import com.spring.sharepod.v1.repository.SearchForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.spring.sharepod.entity.QAmount.amount;
import static com.spring.sharepod.entity.QBoard.board;
import static com.spring.sharepod.entity.QImgFiles.imgFiles;
import static com.spring.sharepod.entity.QAuth.auth;
import static com.spring.sharepod.entity.QReservation.reservation;
import static com.spring.sharepod.entity.QUser.user;
import static org.aspectj.util.LangUtil.isEmpty;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BoardAllResponseDto> searchFormQuality(SearchForm searchForm) {
        return getBoardBySearchFormQuality(searchForm).fetch();
    }

    @Override
    public List<BoardAllResponseDto> searchFormCost(SearchForm searchForm) {
        return getBoardBySearchFormCost(searchForm).fetch();
    }


    @Override
    public List<BoardAllResponseDto> searchFormRecent(SearchForm searchForm) {
        return getBoardBySearchFormRecent(searchForm).fetch();
    }

    //13번 API 메인 게시판
    @Override
    public List<BoardAllResponseDto> searchAllBoard() {
        return getAllBoard().fetch();
    }

    //5번 API 내가 등록한 글
    @Override
    public List<MyBoardResponseDto> getMyBoard(Long userId) {
        return getMyBoardList(userId).fetch();
    }

    @Override
    public List<RentBuyer> getRentBuyer(Long userId) {
        return getRentBuyerList(userId).fetch();
    }

    @Override
    public List<RentSeller> getRentSeller(Long userId) {
        return getRentSellerList(userId).fetch();
    }


    @Override
    public List<UserReservation> getReservation(Long userId) {
        return getReservationList(userId).fetch();
    }

    @Override
    public List<VideoAllResponseDto> videoAll(int startNum) {
        return getVideoAll(startNum).fetch();
    }

    private JPAQuery<VideoAllResponseDto> getVideoAll(int startNum){
        return jpaQueryFactory.select(Projections.constructor(VideoAllResponseDto.class,
                board.id,
                board.boardRegion,
                board.title,
                imgFiles.videoUrl,
                user.userImg,
                user.nickName
                ))
                .from(board)
                .innerJoin(imgFiles)
                .on(board.id.eq(imgFiles.board.id))
                .innerJoin(user)
                .on(board.user.id.eq(user.id))
                .orderBy(Expressions.numberTemplate(Double.class,"function('rand')").asc())
                .offset(startNum)
                .limit(4);
    }

    //내가 요청한 게시글
    private JPAQuery<UserReservation> getReservationList(Long userId) {
        return jpaQueryFactory.select(Projections.bean(UserReservation.class,
                        board.id,
                        board.title,
                        board.boardRegion,
                        board.boardTag,
                        imgFiles.firstImgUrl,
                        amount.dailyRentalFee,
                        reservation.startRental,
                        reservation.endRental,
                        reservation.seller.nickName,
                        board.category,
                        reservation.id.as("reservationId")
                ))
                .from(reservation)
                .leftJoin(board)
                .on(reservation.board.id.eq(board.id))
                .innerJoin(imgFiles)
                .on(board.id.eq(imgFiles.board.id))
                .innerJoin(amount)
                .on(imgFiles.board.id.eq(amount.board.id))
                .where(reservation.buyer.id.eq(userId))
                .orderBy(board.modifiedAt.desc());


    }

    //내가 빌린 게시글
    private JPAQuery<RentSeller> getRentSellerList(Long userId) {
        return jpaQueryFactory.select(Projections.bean(RentSeller.class,
                        board.id,
                        board.title,
                        board.boardRegion,
                        board.boardTag,
                        imgFiles.firstImgUrl,
                        amount.dailyRentalFee,
                        auth.startRental,
                        auth.endRental,
                        board.auth.authSeller.nickName,
                        board.category,
                        auth.id.as("authId")
                )).from(board)
                .innerJoin(auth)
                .on(board.auth.id.eq(auth.id))
                .innerJoin(imgFiles)
                .on(auth.board.id.eq(imgFiles.board.id))
                .innerJoin(amount)
                .on(imgFiles.board.id.eq(amount.board.id))
                .where(board.auth.authSeller.id.eq(userId))
                .orderBy(board.modifiedAt.desc());

    }


    //내가 빌린 게시글
    private JPAQuery<RentBuyer> getRentBuyerList(Long userId) {
        return jpaQueryFactory.select(Projections.bean(RentBuyer.class,
                        board.id,
                        board.title,
                        board.boardRegion,
                        board.boardTag,
                        imgFiles.firstImgUrl,
                        amount.dailyRentalFee,
                        auth.startRental,
                        auth.endRental,
                        board.user.nickName,
                        board.category,
                        auth.id.as("authId")
                )).from(board)
                .innerJoin(auth)
                .on(board.auth.id.eq(auth.id))
                .innerJoin(imgFiles)
                .on(auth.board.id.eq(imgFiles.board.id))
                .innerJoin(amount)
                .on(imgFiles.board.id.eq(amount.board.id))
                .where(board.auth.authBuyer.id.eq(userId))
                .orderBy(board.modifiedAt.desc());

    }

    //내가 등록한 게시글
    private JPAQuery<MyBoardResponseDto> getMyBoardList(Long userId) {
        return jpaQueryFactory.select(Projections.bean(MyBoardResponseDto.class,
                        board.id,
                        board.title,
                        board.boardTag,
                        board.boardRegion,
                        imgFiles.firstImgUrl,
                        board.modifiedAt,
                        amount.dailyRentalFee,
                        board.user.nickName,
                        board.category
                ))
                .from(board)
                .innerJoin(imgFiles)
                .on(board.id.eq(imgFiles.board.id))
                .innerJoin(amount)
                .on(imgFiles.board.id.eq(amount.board.id))
                .where(board.user.id.eq(userId),
                        board.mainAppear.eq(true))
                .orderBy(board.modifiedAt.desc());
    }

    //메인 페이지 8개
    private JPAQuery<BoardAllResponseDto> getAllBoard() {
        return jpaQueryFactory
                .select(Projections.bean(BoardAllResponseDto.class,
                        board.id,
                        imgFiles.firstImgUrl,
                        board.title,
                        board.category,
                        amount.dailyRentalFee,
                        board.boardRegion,
                        board.boardTag,
                        board.modifiedAt
                ))
                .from(board)
                .innerJoin(imgFiles)
                .on(board.id.eq(imgFiles.board.id))
                .innerJoin(amount)
                .on(imgFiles.board.id.eq(amount.board.id))
                .where(board.mainAppear.eq(true))
                .orderBy(board.modifiedAt.desc())
                .limit(8);
    }

//    private BooleanExpression userId(Optional<Long> userId) {
//        System.out.println(userId);
//        return isEmpty(String.valueOf(userId)) ? null : userId.get();
//    }

    //searchFormRecent
    private JPAQuery<BoardAllResponseDto> getBoardBySearchFormRecent(SearchForm searchForm) {
        return jpaQueryFactory
                .select(Projections.bean(BoardAllResponseDto.class, board.id, imgFiles.firstImgUrl, board.title, board.category, amount.dailyRentalFee, board.boardRegion, board.boardTag, board.modifiedAt))
                .from(board)
                .innerJoin(imgFiles)
                .on(board.id.eq(imgFiles.board.id))
                .innerJoin(amount)
                .on(imgFiles.board.id.eq(amount.board.id))
                .orderBy(board.modifiedAt.desc())
                .offset(searchForm.getStartNum())
                .limit(16)
                .where(
                        // 3.
                        searchTitle(searchForm.getSearchTitle()),
                        boardRegion(searchForm.getBoardRegion()),
                        category(searchForm.getCategory()),
                        board.mainAppear.eq(true)
                        // ...
                );
    }

    //searchFormRecent
    private JPAQuery<BoardAllResponseDto> getBoardBySearchFormCost(SearchForm searchForm) {
        return jpaQueryFactory
                .select(Projections.bean(BoardAllResponseDto.class, board.id, board.imgFiles.firstImgUrl, board.title, board.category, board.amount.dailyRentalFee, board.boardRegion, board.boardTag, board.modifiedAt))
                .from(board)
                .orderBy(board.amount.dailyRentalFee.desc())
                .offset(searchForm.getStartNum())
                .limit(16)
                .where(
                        // 3.
                        searchTitle(searchForm.getSearchTitle()),
                        boardRegion(searchForm.getBoardRegion()),
                        category(searchForm.getCategory()),
                        board.mainAppear.eq(true)
                        // ...
                );
    }

    //searchFormRecent
    private JPAQuery<BoardAllResponseDto> getBoardBySearchFormQuality(SearchForm searchForm) {
        return jpaQueryFactory
                .select(Projections.bean(BoardAllResponseDto.class, board.id, board.imgFiles.firstImgUrl, board.title, board.category, board.amount.dailyRentalFee, board.boardRegion, board.boardTag, board.modifiedAt))
                .from(board)
                .orderBy(board.productQuality.desc())
                .offset(searchForm.getStartNum())
                .limit(16)
                .where(
                        // 3.
                        searchTitle(searchForm.getSearchTitle()),
                        boardRegion(searchForm.getBoardRegion()),
                        category(searchForm.getCategory()),
                        board.mainAppear.eq(true)
                        // ...
                );
    }


    //동적 쿼리를 위한 문들
    private BooleanExpression boardRegion(String boardRegion) {
        return isEmpty(boardRegion) ? null : board.boardRegion.eq(boardRegion);
    }


    private BooleanExpression category(String category) {
        return isEmpty(category) ? null : board.category.eq(category);
    }

    private BooleanExpression searchTitle(String searchTitle) {
        return isEmpty(searchTitle) ? null : board.title.contains(searchTitle);
    }

    private OrderSpecifier<?> filterType(String filterType) {
        return filterType.equals("cost") ? null : board.amount.dailyRentalFee.asc();
    }
}
