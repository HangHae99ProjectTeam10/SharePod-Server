package com.spring.sharepod.validator;


import com.spring.sharepod.dto.request.Board.BoardPatchRequestDTO;
import com.spring.sharepod.dto.request.Board.BoardWriteRequestDTO;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.spring.sharepod.exception.ErrorCode.*;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class BoardValidator {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

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
