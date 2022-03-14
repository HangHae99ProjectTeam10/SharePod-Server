package com.spring.sharepod.validator;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.LikedRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.spring.sharepod.exception.ErrorCode.BOARD_NOT_FOUND;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class BoardValidator {
    private final BoardRepository boardRepository;
    private final TokenValidator tokenValidator;
    private final LikedRepository likedRepository;

    //기본 주소 데이터
    public String DefaultMapData(Optional<String> mapdata) {
        String defaultMapdata = null;
        if (!mapdata.isPresent()) {
            return defaultMapdata = "중구";
        } else {
            return defaultMapdata = String.valueOf(mapdata);
        }
    }

    //기본 filter 데이정
    public String DefaultFilterData(Optional<String> filtertype) {
        String defaultFilter = null;
        if (!filtertype.isPresent()) {
            return defaultFilter = "";
        } else {
            return defaultFilter = String.valueOf(filtertype);
        }
    }

    //기본 카테고리 데이터
    public String DefaultCategoryData(Optional<String> category) {
        String defaultCategory = null;
        if (!category.isPresent()) {
            return defaultCategory = "전자제품";
        } else {
            return defaultCategory = String.valueOf(category);
        }
    }

    //기본 검색 데이터
    public String DefaultSearchData(Optional<String> searchtitle) {
        String defaultSearchTitle = null;
        if (!searchtitle.isPresent()) {
            return defaultSearchTitle = "recent";
        } else {
            return defaultSearchTitle = String.valueOf(searchtitle);
        }
    }

    // 기본 limit count 갯수(5)개 설정
    public Long DefaultLimitCount(Long limitcount) {
        Long defaultLimitCount = null;
        if (limitcount == null) {
            return defaultLimitCount = 5L;
        } else {
            return defaultLimitCount = limitcount;
        }
    }

    //상세 페이지 보여줄 시 로그인이 되어 있을 경우, 찜하기가 되어있는지에 대한 판단
    public Boolean DefaultLiked(Optional<Long> userid, Long boardid) {
        // isliked가 null일때는 로그인을 하지 않은 유저이므로 찜하기 부분을 False로 처리한다.
        Boolean isliked = null;
        if (!userid.isPresent()) {
            return isliked = false;
        } else {
            //userid.get().longValue()이 존재하므로
            tokenValidator.userIdCompareToken(userid.get().longValue());
            Liked liked = likedRepository.findByLiked(boardid, userid.get().longValue());
            if (liked == null) {
                return isliked = false;
            } else {
                return isliked = true;
            }
        }
    }

    //상세 페이지를 보여줄 떄, board가 존재하는지 아닌지에 대한 판단
    public Board ValidByBoardId(Long boardid) {
        return boardRepository.findById(boardid).orElseThrow(
                () -> new ErrorCodeException(BOARD_NOT_FOUND));
    }


    //



}
