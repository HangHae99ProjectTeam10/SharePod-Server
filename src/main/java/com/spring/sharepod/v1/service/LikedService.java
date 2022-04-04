package com.spring.sharepod.v1.service;

import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.v1.dto.request.LikeRequestDTO;
import com.spring.sharepod.v1.dto.response.Liked.LikedResponseDto;
import com.spring.sharepod.v1.repository.Liked.LikedRepository;
import com.spring.sharepod.v1.validator.BoardValidator;
import com.spring.sharepod.v1.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikedService {
    private final LikedRepository likedRepository;
    private final BoardValidator boardValidator;
    private final UserValidator userValidator;

    //16번 찜하기, 찜하기 취소
    @Transactional
    public LikedResponseDto.LikedPost islikeservice(Long boardid, LikeRequestDTO.Liked requestDTO) {
        //찜할 게시판 boardid로 검색해 가져오기
        Board board = boardValidator.ValidByBoardId(boardid);

        //찜할 유저 userid로 검색해 가져오기
        User user = userValidator.ValidByUserId(requestDTO.getUserId());

        //해당 like 가져오기
        Liked like = likedRepository.findByUserAndBoard(user, board);

        //like에 값이 존재하지 않으면 추가
        if (like == null){
            likedRepository.save(Liked.builder()
                    .user(user)
                    .board(board)
                    .build()).getId();
            return LikedResponseDto.LikedPost.builder()
                    .result("success")
                    .msg(board.getTitle()+ "찜하기 완료")
                    .userId(requestDTO.getUserId())
                    .boardId(boardid)
                    .build();

        }
        //like에 값이 존재하면 삭제
        else {
            likedRepository.deleteById(like.getId());

            return LikedResponseDto.LikedPost.builder()
                    .result("success")
                    .msg(board.getTitle()+ "찜하기 취소 완료")
                    .userId(requestDTO.getUserId())
                    .boardId(boardid)
                    .build();
        }
    }
}
