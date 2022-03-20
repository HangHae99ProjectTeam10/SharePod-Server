package com.spring.sharepod.v1.validator;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.dto.request.BoardRequestDto;
import com.spring.sharepod.v1.repository.BoardRepository;
import com.spring.sharepod.v1.repository.LikedRepository;
import com.spring.sharepod.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

import static com.spring.sharepod.exception.CommonError.ErrorCode.*;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class BoardValidator {
    private final BoardRepository boardRepository;
    private final TokenValidator tokenValidator;
    private final LikedRepository likedRepository;
    private final UserRepository userRepository;

    //기본 검색 데이터
    public String DefaultSearchData(Optional<String> searchtitle) {
        String defaultSearchTitle = null;
        if (!searchtitle.isPresent()) {
            return defaultSearchTitle = "";
        } else {
            return defaultSearchTitle = String.valueOf(searchtitle.get());
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
            Liked liked = likedRepository.findByLiked(boardid, userid.get().longValue());
            if (liked == null) {
                return isliked = false;
            } else {
                return isliked = true;
            }
        }
    }

    //상세 페이지를 보여줄 떄, board가 존재하는지 아닌지에 대한 판단
    public Board ValidByBoardId(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new ErrorCodeException(BOARD_NOT_FOUND));
    }


    public void validateBoardWrite(BoardRequestDto.WriteBoard boardWriteRequestDTO,
                                   MultipartFile[] imgfiles,
                                   MultipartFile videofile){
        // 유저네임(이메일) 유무 확인
        userRepository.findById(boardWriteRequestDTO.getUserId()).orElseThrow(
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
        if(boardWriteRequestDTO.getOriginPrice() < 0){
            throw new ErrorCodeException(BOARD_ORIGINPRICE_NOT_EXIST);
        }
        //일일대여료 적었는지 확인
        if(boardWriteRequestDTO.getDailyRentalFee() < 0){
            throw new ErrorCodeException(BOARD_DAILYRENTALFEE_NOT_EXIST);
        }
        //지역 적었는지 확인
        if(Objects.equals(boardWriteRequestDTO.getBoardRegion(), "")){
            throw new ErrorCodeException(BOARD_MAPDATA_NOT_EXIST);
        }
        //상품태그를 적었는지 확인
        if(Objects.equals(boardWriteRequestDTO.getCategory(), "")){
            throw new ErrorCodeException(BOARD_CATEGORY_NOT_EXIST);
        }
        //상품태그를 적었는지 확인
        if(Objects.equals(boardWriteRequestDTO.getProductQuality(), "")){
            throw new ErrorCodeException(BOARD_BOARDQUILITY_NOT_EXIST);
        }

        //이미지 파일이 첫번째 오지 않았을 때
        if(Objects.equals(imgfiles[0].getOriginalFilename(), "")){
            throw new ErrorCodeException(BOARD_MAIN_IMGFILE_NOT_EXIST);
        }

//        //이미지 파일이 있는지 확인
//        for (int i = 0; i < imgfiles.length; i++){
//            if(Objects.equals(imgfiles[i].getOriginalFilename(), "")){
//                throw new ErrorCodeException(BOARD_IMG_NOT_EXIST);
//            }
//        }

        //비디오파일 있는지 확인
//        if(Objects.equals(videofile.getOriginalFilename(), "")){
//            throw new ErrorCodeException(VIDEOFILE_NOT_EXIST);
//        }
//
//
//        for (int i=0; i<imgfiles.length; i++){
//
//        }
//        // 파일의 확장자 추출
//        String originalFileExtension;
//        String contentType = imgfiles.getContentType();
//
//        // 확장자명이 존재하지 않을 경우 처리 x
//        if(ObjectUtils.isEmpty(contentType)) {
//            break;
//        }
//        else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
//            if(contentType.contains("image/jpeg"))
//                originalFileExtension = ".jpg";
//            else if(contentType.contains("image/png"))
//                originalFileExtension = ".png";
//            else  // 다른 확장자일 경우 처리 x
//                break;
//        }


    }

    public void validateBoardUpdate(BoardRequestDto.PatchBoard patchRequestDTO){

        // 유저네임(이메일) 유무 확인
        userRepository.findById(patchRequestDTO.getUserId()).orElseThrow(
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
        if(patchRequestDTO.getOriginPrice() < 0){
            throw new ErrorCodeException(BOARD_ORIGINPRICE_NOT_EXIST);
        }
        //일일대여료 적었는지 확인
        if(patchRequestDTO.getDailyRentalFee() < 0){
            throw new ErrorCodeException(BOARD_DAILYRENTALFEE_NOT_EXIST);
        }
        //지역 적었는지 확인
        if(Objects.equals(patchRequestDTO.getBoardRegion(), "")){
            throw new ErrorCodeException(BOARD_MAPDATA_NOT_EXIST);
        }
        //상품태그를 적었는지 확인
        if(Objects.equals(patchRequestDTO.getCategory(), "")){
            throw new ErrorCodeException(BOARD_CATEGORY_NOT_EXIST);
        }
        //상품태그를 적었는지 확인
        if(Objects.equals(patchRequestDTO.getProductQuality(), "")){
            throw new ErrorCodeException(BOARD_BOARDQUILITY_NOT_EXIST);
        }

    }

}
