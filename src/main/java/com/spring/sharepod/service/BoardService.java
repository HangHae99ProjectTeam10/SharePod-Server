package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.Board.BoardPatchRequestDTO;
import com.spring.sharepod.dto.request.Board.BoardWriteRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Board.BoardAllResponseDto;
import com.spring.sharepod.dto.response.Board.BoardDetailResponseDto;
import com.spring.sharepod.dto.response.Board.VideoAllResponseDto;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.LikedRepository;
import com.spring.sharepod.repository.UserRepository;
import com.spring.sharepod.validator.BoardValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.spring.sharepod.exception.CommonError.ErrorCode.BOARD_NOT_FOUND;
import static com.spring.sharepod.exception.CommonError.ErrorCode.BOARD_NOT_FOUND2;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikedRepository likedRepository;
    private final BoardValidator boardValidator;
    private final AwsS3Service awsS3Service;


    //메인 페이지 전체 게시글 불러오기
    @Transactional
    public List<BoardAllResponseDto> getAllBoard(Long limitcount) {

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

    //정렬한 board, GET 방식
    @Transactional
    public List<BoardAllResponseDto> getSortedBoard(String filtertype, String category, String mapdata, Long limitcount) {
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
    public List<BoardAllResponseDto> getSearchBoard(String filtertype, String searchtitle, String mapdata, Long limitcount) {
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
        // 반환되는 DTO
        return boardResponseDtos;
    }


    // 상세 페이지 board GET
    @Transactional
    public BoardDetailResponseDto getDetailBoard(Long boardid, Boolean isliked) {
        //보드가 존재하지 않을 시 메시지 호출
        Board boardDetail = boardValidator.ValidByBoardId(boardid);

        // 존재한다면 받아온 내용들을 담아서 보내주기
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

    //릴스 video 전체 GET(Limit)
    @Transactional
    public List<VideoAllResponseDto> getAllVideo(Long limitcount) {
        // 모든 릴스 가져오기
        List<Board> boardList = boardRepository.findAllByVideoUrlRan(limitcount);

        // 릴스를 반환해서 저장할 리스트
        List<VideoAllResponseDto> videoAllResponseDtos = new ArrayList<>();

        // 릴스 덩어리 해쳐서 넣어주기
        for (Board board : boardList) {
            String nickname = board.getUser().getNickname();
            String userimg = board.getUser().getUserimg();


            // videoAllResponseDto 생성
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
    public BasicResponseDTO updateboard(Long boardid, BoardPatchRequestDTO patchRequestDTO) {

        //수정할 게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );
        //받아온 userid와 boardid의 작성자가 다를때
        if (!Objects.equals(patchRequestDTO.getUserid(), board.getUser().getId())) {
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
    public BasicResponseDTO deleteboard(Long boardid, Long userid) {

        //삭제할 게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardid).orElseThrow(
                () -> new ErrorCodeException(BOARD_NOT_FOUND)
        );
        //받아온 userid와 boardid의 작성자가 다를때
        if (!Objects.equals(userid, board.getUser().getId())) {
            throw new ErrorCodeException(BOARD_NOT_FOUND2);
        }

        //S3 상의 키 값을 얻기 위한 substring
        String boardimg1 = board.getImgurl1().substring(board.getImgurl1().lastIndexOf("/")+1);
        System.out.println("boardimg1 key" + boardimg1);
        String boardimg2 = board.getImgurl2().substring(board.getImgurl2().lastIndexOf("/")+1);
        System.out.println("boardimg2 key" + boardimg2);
        String boardimg3 = board.getImgurl3().substring(board.getImgurl3().lastIndexOf("/")+1);
        System.out.println("boardimg3 key" + boardimg3);

        String videourl = board.getVideourl().substring(board.getVideourl().lastIndexOf("/")+1);
        System.out.println("video key" + videourl);

        // 리스트에 담에서 넣어주기
//        String[] imgs = {videourl,boardimg1,boardimg2,boardimg3};
//        List<String> fileName = Arrays.asList(imgs);
        //     String fileName = videourl;

        //S3 상 이미지 삭제 호출
//        s3Service.fileDelete(fileName);
        //s3Service.fileDeleteOne(fileName);

        String[] imgs = {boardimg1,boardimg2,boardimg3, videourl};
        List<String> fileName = Arrays.asList(imgs);
        awsS3Service.deleteFile(fileName);



        //게시글 삭제
        boardRepository.deleteById(boardid);

        return BasicResponseDTO.builder()
                .result("success")
                .msg("삭제 완료")
                .build();
    }


    //게시판 작성
    @Transactional
    public BasicResponseDTO wirteboard(BoardWriteRequestDTO boardWriteRequestDTO) {


        User user = userRepository.findById(boardWriteRequestDTO.getUserid()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));


        //보드 저장
        boardRepository.save(Board.builder()
                .user(user)
                .title(boardWriteRequestDTO.getTitle())
                .videourl(boardWriteRequestDTO.getVideourl())
                .imgurl1(boardWriteRequestDTO.getImgurl1())
                .imgurl2(boardWriteRequestDTO.getImgurl2())
                .imgurl3(boardWriteRequestDTO.getImgurl3())
                .contents(boardWriteRequestDTO.getContents())
                .originprice(boardWriteRequestDTO.getOriginprice())
                .dailyrentalfee(boardWriteRequestDTO.getDailyrentalfee())
                .mapdata(boardWriteRequestDTO.getMapdata())
                .category(boardWriteRequestDTO.getCategory())
                .boardquility(boardWriteRequestDTO.getBoardquility())
                .appear(true)
                .build());

        return BasicResponseDTO.builder()
                .result("success")
                .msg("이미지 저장 완료")
                .build();
    }
}
