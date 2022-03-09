package com.spring.sharepod.service;


import com.spring.sharepod.dto.request.BoardPatchRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.exception.ErrorCode;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시판 수정
    @Transactional
    public BasicResponseDTO updateboard(Long boardid, BoardPatchRequestDTO patchRequestDTO){

        //수정할 게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardid).orElseThrow(
                () ->new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );
        //받아온 userid와 boardid의 작성자가 다를때
        if (!Objects.equals(patchRequestDTO.getUserid(),board.getUser().getId())){
            throw new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND2);
        }

        //게시판 업데이트
        board.update(patchRequestDTO);

        return BasicResponseDTO.builder()
                .result("success")
                .msg("수정 완료")
                .build();
    }

    //게시판 삭제
    @Transactional
    public BasicResponseDTO deleteboard(Long boardid, Long userid){

        //삭제할 게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardid).orElseThrow(
                () ->new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );
        //받아온 userid와 boardid의 작성자가 다를때
        if (!Objects.equals(userid,board.getUser().getId())){
            throw new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND2);
        }

        //게시글 삭제
        boardRepository.deleteById(boardid);

        return BasicResponseDTO.builder()
                .result("success")
                .msg("수정 완료")
                .build();
    }
}
