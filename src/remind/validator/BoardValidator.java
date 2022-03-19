package com.spring.sharepod.validator;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.LikedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.spring.sharepod.exception.CommonError.ErrorCode.BOARD_NOT_FOUND;
import com.spring.sharepod.dto.request.Board.BoardPatchRequestDTO;
import com.spring.sharepod.dto.request.Board.BoardWriteRequestDTO;
import com.spring.sharepod.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.spring.sharepod.exception.CommonError.ErrorCode.*;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class BoardValidator {
    private final BoardRepository boardRepository;
    private final TokenValidator tokenValidator;
    private final LikedRepository likedRepository;
    private final UserRepository userRepository;

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
    public Boolean DefaultLiked(Optional<Long> userid, Long boardid, User user) {
        // isliked가 null일때는 로그인을 하지 않은 유저이므로 찜하기 부분을 False로 처리한다.
        Boolean isliked = null;
        if (!userid.isPresent()) {
            return isliked = false;
        } else {
            //userid.get().longValue()이 존재하므로
            tokenValidator.userIdCompareToken(userid.get().longValue(),user.getId());
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



    public void validateBoardWrite(BoardWriteRequestDTO boardWriteRequestDTO,
                                   MultipartFile[] imgfiles,
                                   MultipartFile videofile){
        // 유저네임(이메일) 유무 확인
        userRepository.findById(boardWriteRequestDTO.getUserid()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

        //제목 적었는지 확인
        if(Objects.equals(boardWriteRequestDTO.getTitle(), "")){
            throw new ErrorCodeException(BOARD_TITLE_NOT_EXIST);
        }
        //내용 적었는지 확인
        if(Objects.equals(boardWriteRequestDTO.getContents(), "")){
            throw new ErrorCodeException(BOARD_CONTENTS_NOT_EXIST);
        }
        //원가 적었는지 확인
        if(boardWriteRequestDTO.getOriginprice() < 0){
            throw new ErrorCodeException(BOARD_ORIGINPRICE_NOT_EXIST);
        }
        //일일대여료 적었는지 확인
        if(boardWriteRequestDTO.getDailyrentalfee() < 0){
            throw new ErrorCodeException(BOARD_DAILYRENTALFEE_NOT_EXIST);
        }
        //지역 적었는지 확인
        if(Objects.equals(boardWriteRequestDTO.getMapdata(), "")){
            throw new ErrorCodeException(BOARD_MAPDATA_NOT_EXIST);
        }
        //상품태그를 적었는지 확인
        if(Objects.equals(boardWriteRequestDTO.getCategory(), "")){
            throw new ErrorCodeException(BOARD_CATEGORY_NOT_EXIST);
        }
        //상품태그를 적었는지 확인
        if(Objects.equals(boardWriteRequestDTO.getBoardquility(), "")){
            throw new ErrorCodeException(BOARD_BOARDQUILITY_NOT_EXIST);
        }

        //이미지 파일이 3개 오지 않았을 때
        if(imgfiles.length != 3){
            throw new ErrorCodeException(BOARD_IMGFILE3_NOT_EXIST);
        }
        //이미지 파일이 있는재 확인
        for (int i = 0; i < imgfiles.length; i++){
            if(Objects.equals(imgfiles[i].getOriginalFilename(), "")){
                throw new ErrorCodeException(BOARD_IMG_NOT_EXIST);
            }
        }

        //비디오파일 있는지 확인
        if(Objects.equals(videofile.getOriginalFilename(), "")){
            throw new ErrorCodeException(VIDEOFILE_NOT_EXIST);
        }

    }

    public void validateBoardUpdate(BoardPatchRequestDTO patchRequestDTO){

        // 유저네임(이메일) 유무 확인
        userRepository.findById(patchRequestDTO.getUserid()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

        //제목 적었는지 확인
        if(Objects.equals(patchRequestDTO.getTitle(), "")){
            throw new ErrorCodeException(BOARD_TITLE_NOT_EXIST);
        }
        //내용 적었는지 확인
        if(Objects.equals(patchRequestDTO.getContents(), "")){
            throw new ErrorCodeException(BOARD_CONTENTS_NOT_EXIST);
        }
        //원가 적었는지 확인
        if(patchRequestDTO.getOriginprice() < 0){
            throw new ErrorCodeException(BOARD_ORIGINPRICE_NOT_EXIST);
        }
        //일일대여료 적었는지 확인
        if(patchRequestDTO.getDailyrentalfee() < 0){
            throw new ErrorCodeException(BOARD_DAILYRENTALFEE_NOT_EXIST);
        }
        //지역 적었는지 확인
        if(Objects.equals(patchRequestDTO.getMapdata(), "")){
            throw new ErrorCodeException(BOARD_MAPDATA_NOT_EXIST);
        }
        //상품태그를 적었는지 확인
        if(Objects.equals(patchRequestDTO.getCategory(), "")){
            throw new ErrorCodeException(BOARD_CATEGORY_NOT_EXIST);
        }
        //상품태그를 적었는지 확인
        if(Objects.equals(patchRequestDTO.getBoardquility(), "")){
            throw new ErrorCodeException(BOARD_BOARDQUILITY_NOT_EXIST);
        }

    }

}
