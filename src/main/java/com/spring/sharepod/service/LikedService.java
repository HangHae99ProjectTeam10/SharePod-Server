package com.spring.sharepod.service;


import com.spring.sharepod.dto.request.Liked.LikeRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.LikedRepository;
import com.spring.sharepod.repository.UserRepository;
import com.spring.sharepod.validator.BoardValidator;
import com.spring.sharepod.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikedService {
    private final LikedRepository likedRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardValidator boardValidator;
    private final UserValidator userValidator;

    @Transactional
    public BasicResponseDTO islikeservice(Long boardid, LikeRequestDTO requestDTO) {

        //찜할 게시판 boardid로 검색해 가져오기
        Board board = boardValidator.ValidByBoardId(boardid);

        //찜할 유저 userid로 검색해 가져오기
        User user = userValidator.ValidByUserId(requestDTO.getUserid());

        //해당 like 가져오기
        Liked like = likedRepository.findByUserAndBoard(user, board);

        //like에 값이 존재하지 않으면 추가
        if (like == null){
            likedRepository.save(Liked.builder()
                    .user(user)
                    .board(board)
                    .build()).getId();

        }
        //like에 값이 존재하면 삭제
        else {
            likedRepository.deleteById(like.getId());
        }

        return BasicResponseDTO.builder()
                .result("success")
                .msg("찜하기 처리 완료")
                .build();
    }

}
