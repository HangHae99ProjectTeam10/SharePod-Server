package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.Board.BoardFilterAndCategoryRequestDto;
import com.spring.sharepod.dto.request.Board.BoardPatchRequestDTO;
import com.spring.sharepod.dto.request.Board.SearchRequestDto;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Board.BoardAllResponseDto;
import com.spring.sharepod.dto.response.Board.BoardDetailResponseDto;
import com.spring.sharepod.dto.response.Board.VideoAllResponseDto;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.exception.ErrorCode;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.LikedRepository;
import com.spring.sharepod.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.spring.sharepod.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.spring.sharepod.exception.ErrorCode.BOARD_NOT_FOUND2;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikedRepository likedRepository;

    @Transactional
    public List<BoardAllResponseDto> getAllBoard(int limitcount) {

        // 모든 게시글 가져오기
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc(limitcount);

        // 게시글을 반환해서 저장할 리스트
        List<BoardAllResponseDto> boardResponseDtos = new ArrayList<>();

        // 게시글 덩어리 해쳐서 넣어주기
        for (Board board : boardList) {
            String nickname = board.getUser().getNickname();
            // BoardResponseDto 생성
            BoardAllResponseDto boardResponseDto = BoardAllResponseDto.builder()
                    .boardid(board.getId())
                    .category(board.getCategory())
                    .title(board.getTitle())
                    .imgurl(board.getImgurl1())
                    .dailyrentalfee(board.getDailyrentalfee())
                    .nickname(nickname)
                    .build();

            // 반환할 리스트에 저장하기
            boardResponseDtos.add(boardResponseDto);
        }

        return boardResponseDtos;
    }

    //
    @Transactional
    public List<BoardAllResponseDto> getSortedBoard(BoardFilterAndCategoryRequestDto boardFilterAndCategoryRequestDto, int limitcount) {
        String mapdata = boardFilterAndCategoryRequestDto.getMapdata();
        String filtertype = boardFilterAndCategoryRequestDto.getFiltertype();
        String category = boardFilterAndCategoryRequestDto.getCategory();

        List<Board> boardList = new ArrayList<>();

        switch (filtertype) {
            case "quility":
                boardList = boardRepository.findByAndMapAndCategoryByQuility(mapdata, category, limitcount);
                break;

            case "cost":
                boardList = boardRepository.findByAndMapAndCategoryByCost(mapdata, category, limitcount);
                break;

            default:
                boardList = boardRepository.findByAndMapAndCategoryByCreatedAt(mapdata, category, limitcount);

        }
        // 게시글을 반환해서 저장할 리스트
        List<BoardAllResponseDto> boardResponseDtos = new ArrayList<>();

        // 게시글 덩어리 해쳐서 넣어주기
        for (Board board : boardList) {
            String nickname = board.getUser().getNickname();
            // BoardResponseDto 생성
            BoardAllResponseDto boardResponseDto = BoardAllResponseDto.builder()
                    .boardid(board.getId())
                    .category(board.getCategory())
                    .title(board.getTitle())
                    .imgurl(board.getImgurl1())
                    .dailyrentalfee(board.getDailyrentalfee())
                    .nickname(nickname)
                    .build();

            // 반환할 리스트에 저장하기
            boardResponseDtos.add(boardResponseDto);
        }

        return boardResponseDtos;
    }


    //검색한 내용에 대한 정보
    @Transactional
    public List<BoardAllResponseDto> getSearchBoard(SearchRequestDto searchRequestDto, int limitcount) {
        String mapdata = searchRequestDto.getMapdata();
        String filtertype = searchRequestDto.getFiltertype();
        String searchtitle = searchRequestDto.getSearchtitle();

        List<Board> boardList = new ArrayList<>();

        switch (filtertype) {
            case "quility":
                boardList = boardRepository.findByAndMapAndSearchByQuility(mapdata, searchtitle, limitcount);
                break;

            case "cost":
                boardList = boardRepository.findByAndMapAndSearchByCost(mapdata, searchtitle, limitcount);
                break;

            default:
                boardList = boardRepository.findByAndMapAndSearchByCreatedAt(mapdata, searchtitle, limitcount);

        }
        // 게시글을 반환해서 저장할 리스트
        List<BoardAllResponseDto> boardResponseDtos = new ArrayList<>();

        // 게시글 덩어리 해쳐서 넣어주기
        for (Board board : boardList) {
            String nickname = board.getUser().getNickname();
            // BoardResponseDto 생성
            BoardAllResponseDto boardResponseDto = BoardAllResponseDto.builder()
                    .boardid(board.getId())
                    .category(board.getCategory())
                    .title(board.getTitle())
                    .imgurl(board.getImgurl1())
                    .dailyrentalfee(board.getDailyrentalfee())
                    .nickname(nickname)
                    .build();

            // 반환할 리스트에 저장하기
            boardResponseDtos.add(boardResponseDto);
        }

        return boardResponseDtos;
    }

    @Transactional
    public BoardDetailResponseDto getDetailBoard(Long boardid, Long userid) {
        Boolean isliked = null;
        if (userid  == null){
            isliked = false;
        }else{
            Liked liked = likedRepository.findByLiked(boardid, userid);

            if (liked == null){
                isliked = false;
            }else{
                isliked = true;
            }
        }
        Board boardDetail = boardRepository.findById(boardid).orElseThrow(()-> new ErrorCodeException(BOARD_NOT_FOUND));

        BoardDetailResponseDto boardDetailResponseDto = BoardDetailResponseDto.builder()
                .title(boardDetail.getTitle())
                .videourl(boardDetail.getVideourl())
                .imgurl1(boardDetail.getImgurl1())
                .imgurl2(boardDetail.getImgurl2())
                .imgurl3(boardDetail.getImgurl3())
                .contents(boardDetail.getContents())
                .originprice(boardDetail.getOriginprice())
                .dailyrentalfee(boardDetail.getDailyrentalfee())
                .nickname(boardDetail.getUser().getNickname())
                .mapdata(boardDetail.getMapdata())
                .category(boardDetail.getCategory())
                .boardquility(boardDetail.getBoardquility())
                .isliked(isliked)
                .likecount(boardDetail.getLikeNumber().size())
                .userimg(boardDetail.getUser().getUserimg())
                .modifiedat(String.valueOf(boardDetail.getModifiedAt()))
                .build();

        return boardDetailResponseDto;
    }

    @Transactional
    public List<VideoAllResponseDto> getAllVideo(int limitcount) {
        // 모든 게시글 가져오기
        List<Board> boardList = boardRepository.findAllByVideoUrlRan(limitcount);

        // 게시글을 반환해서 저장할 리스트
        List<VideoAllResponseDto> videoAllResponseDtos = new ArrayList<>();

        // 게시글 덩어리 해쳐서 넣어주기
        for (Board board : boardList) {
            String nickname = board.getUser().getNickname();
            String userimg = board.getUser().getUserimg();


            // BoardResponseDto 생성
            VideoAllResponseDto videoAllResponseDto = VideoAllResponseDto.builder()
                    .boardid(board.getId())
                    .videourl(board.getVideourl())
                    .title(board.getTitle())
                    .userimg(userimg)
                    .nickname(nickname)
                    .build();

            // 반환할 리스트에 저장하기
            videoAllResponseDtos.add(videoAllResponseDto);
        }

        return videoAllResponseDtos;
    }


    // 게시판 수정
    @Transactional
    public BasicResponseDTO updateboard(Long boardid, BoardPatchRequestDTO patchRequestDTO){

        //수정할 게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardid).orElseThrow(
                () ->new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );
        //받아온 userid와 boardid의 작성자가 다를때
        if (!Objects.equals(patchRequestDTO.getUserid(),board.getUser().getId())){
            throw new ErrorCodeException(BOARD_NOT_FOUND2);
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
                () ->new ErrorCodeException(BOARD_NOT_FOUND)
        );
        //받아온 userid와 boardid의 작성자가 다를때
        if (!Objects.equals(userid,board.getUser().getId())){
            throw new ErrorCodeException(BOARD_NOT_FOUND2);
        }

        //게시글 삭제
        boardRepository.deleteById(boardid);

        return BasicResponseDTO.builder()
                .result("success")
                .msg("삭제 완료")
                .build();

    }
}
