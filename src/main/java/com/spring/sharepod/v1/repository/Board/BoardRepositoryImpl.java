package com.spring.sharepod.v1.repository.Board;


import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.sharepod.v1.dto.response.BoardAllResponseDto;
import com.spring.sharepod.v1.repository.SearchForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.sharepod.entity.QBoard.board;
import static org.aspectj.util.LangUtil.isEmpty;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
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

    @Override
    public List<BoardAllResponseDto> searchAllBoard() {
        return getAllBoard().fetch();
    }


    private JPAQuery<BoardAllResponseDto> getAllBoard() {
        return jpaQueryFactory
                .select(Projections.bean(BoardAllResponseDto.class,
                        board.id,
                        board.imgFiles.firstImgUrl,
                        board.title,board.category,
                        board.amount.dailyRentalFee,
                        board.boardRegion,
                        board.boardTag,
                        board.modifiedAt
                ))
                .from(board)
                .orderBy(board.modifiedAt.asc())
                .limit(8);
    }

//    private BooleanExpression userId(Optional<Long> userId) {
//        System.out.println(userId);
//        return isEmpty(String.valueOf(userId)) ? null : userId.get();
//    }


    private JPAQuery<BoardAllResponseDto> getBoardBySearchFormRecent(SearchForm searchForm) {
        return jpaQueryFactory
                .select(Projections.constructor(BoardAllResponseDto.class,board.id,board.imgFiles.firstImgUrl,board.title,board.category,board.amount.dailyRentalFee,board.boardRegion,board.boardTag,board.modifiedAt))
                .from(board)
                .orderBy(board.modifiedAt.asc())
                .offset(searchForm.getStartNum())
                .limit(16)
                .where(
                        // 3.
                        searchTitle(searchForm.getSearchTitle()),
                        boardRegion(searchForm.getBoardRegion()),
                        category(searchForm.getCategory()

                        )
                        // ...
                );
    }

    private JPAQuery<BoardAllResponseDto> getBoardBySearchFormCost(SearchForm searchForm) {
        return jpaQueryFactory
                .select(Projections.constructor(BoardAllResponseDto.class,board.id,board.imgFiles.firstImgUrl,board.title,board.category,board.amount.dailyRentalFee,board.boardRegion,board.boardTag,board.modifiedAt))
                .from(board)
                .orderBy(board.amount.dailyRentalFee.asc())
                .offset(searchForm.getStartNum())
                .limit(16)
                .where(
                        // 3.
                        searchTitle(searchForm.getSearchTitle()),
                        boardRegion(searchForm.getBoardRegion()),
                        category(searchForm.getCategory()
                        )
                        // ...
                );
    }

    private JPAQuery<BoardAllResponseDto> getBoardBySearchFormQuality(SearchForm searchForm) {
        return jpaQueryFactory
                .select(Projections.constructor(BoardAllResponseDto.class,board.id,board.imgFiles.firstImgUrl,board.title,board.category,board.amount.dailyRentalFee,board.boardRegion,board.boardTag,board.modifiedAt))
                .from(board)
                .orderBy(board.productQuality.asc())
                .offset(searchForm.getStartNum())
                .limit(16)
                .where(
                        // 3.
                        searchTitle(searchForm.getSearchTitle()),
                        boardRegion(searchForm.getBoardRegion()),
                        category(searchForm.getCategory()
                        )
                        // ...
                );
    }

    private BooleanExpression boardRegion(String boardRegion) {
        System.out.println(boardRegion);
        return isEmpty(boardRegion) ? null : board.boardRegion.eq(boardRegion);
    }

    // 5.
    private BooleanExpression category(String category) {
        return isEmpty(category) ? null : board.category.eq(category);
    }

    private BooleanExpression searchTitle(String searchTitle) {
        System.out.println(searchTitle);
        return isEmpty(searchTitle) ? null : board.title.contains(searchTitle);
    }

    private OrderSpecifier<?> filterType(String filterType){
        return filterType.equals("cost") ? null : board.amount.dailyRentalFee.asc();
    }
}
