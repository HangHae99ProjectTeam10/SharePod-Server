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

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public BasicResponseDTO updateboard(Long boardid, BoardPatchRequestDTO patchRequestDTO){
        BasicResponseDTO basicResponseDTO = new BasicResponseDTO();

        Board board = boardRepository.findById(boardid).orElseThrow(
                () ->new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );


        return basicResponseDTO;
    }
}
