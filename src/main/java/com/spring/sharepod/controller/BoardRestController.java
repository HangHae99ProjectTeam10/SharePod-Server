package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.Board.BoardPatchRequestDTO;
import com.spring.sharepod.dto.request.Board.BoardWriteRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Board.BoardAllResponseDto;
import com.spring.sharepod.dto.response.Board.BoardDetailResponseDto;
import com.spring.sharepod.dto.response.Board.VideoAllResponseDto;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.model.AllVideo;
import com.spring.sharepod.model.BoardDetail;
import com.spring.sharepod.model.BoardList;
import com.spring.sharepod.repository.LikedRepository;
import com.spring.sharepod.service.AwsS3Service;
import com.spring.sharepod.service.BoardService;
import com.spring.sharepod.service.S3Service;
import com.spring.sharepod.validator.BoardValidator;
import com.spring.sharepod.validator.TokenValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor //DI 형태, IoC 컨테이너 생성
@RestController
public class BoardRestController {
    private final BoardService boardService;
    private final LikedRepository likedRepository;
    private final TokenValidator tokenValidator;
    private final BoardValidator boardValidator;
    private final AwsS3Service awsS3Service;
    private final S3Service s3Service;


    //** 9번 게시판 작성
    @PostMapping("/board")
    public BasicResponseDTO writeBoard(@RequestPart BoardWriteRequestDTO boardWriteRequestDTO,
                                       @RequestPart MultipartFile[] imgfiles,
                                       @RequestPart MultipartFile videofile, @AuthenticationPrincipal User user) throws IOException {

        //token과 boardWriteRequestDto의 userid와 비교
        tokenValidator.userIdCompareToken(boardWriteRequestDTO.getUserid(), user.getId());

        //게시판 업로드
        BoardWriteRequestDTO boardWriteRequestDTOadd = s3Service.boardupload(boardWriteRequestDTO, imgfiles, videofile);

        //DB저장 및 리턴
        return boardService.wirteboard(boardWriteRequestDTOadd);
    }

    //** 11번 게시판 수정
    @PatchMapping("/board/{boardid}")
    public BasicResponseDTO updateboardcontroll(@PathVariable Long boardid,
                                                @RequestPart BoardPatchRequestDTO patchRequestDTO,
                                                @RequestPart MultipartFile[] imgfiles,
                                                @RequestPart MultipartFile videofile, @AuthenticationPrincipal User user) throws IOException {

        //token과 patchRequestDto의 userid와 비교
        tokenValidator.userIdCompareToken(patchRequestDTO.getUserid(), user.getId());

        //게시판 수정 업로드
        BoardPatchRequestDTO boardPatchRequestDTOadd = s3Service.boardupdate(boardid, patchRequestDTO, imgfiles, videofile);

        return boardService.updateboard(boardid, boardPatchRequestDTOadd);
    }

    //**게시판 삭제
    @DeleteMapping("/board/{boardid}")
    public BasicResponseDTO deleteboardcontroll(@PathVariable Long boardid, @RequestBody Map<String, Long> user, @AuthenticationPrincipal User tokenUser) {
        //token과 user.get("userid")와 비교
        tokenValidator.userIdCompareToken(user.get("userid"), tokenUser.getId());

        return boardService.deleteboard(boardid, user.get("userid"));
    }

    //메인 전체 상품 최신순 보여주기
    @GetMapping("/board")
    public ResponseEntity<BoardList> getBoardList(@RequestParam(value = "limit", required = false) Long limitcount) {
        Long validLimitCount = boardValidator.DefaultLimitCount(limitcount);
        List<BoardAllResponseDto> boardResponseDtos = boardService.getAllBoard(validLimitCount);
        return new ResponseEntity<>(new BoardList("success", "리스트 최신순 성공", boardResponseDtos), HttpStatus.OK);
    }

    //상품 카테고리 클릭 시, 상세 리스트 페이지로 이동
    @GetMapping("/board/sort")
    public ResponseEntity<BoardList> getSortedBoardList(@RequestParam(value = "limit", required = false) Long limitcount, @RequestParam(value = "filtertype", required = false) Optional<String> filtertype, @RequestParam(value = "category", required = false) Optional<String> category, @RequestParam(value = "mapdata", required = false) Optional<String> mapdata) {
        // 각각의 변수에 대한 default값 설정
        String validmapdata = boardValidator.DefaultMapData(mapdata);
        String validFilterData = boardValidator.DefaultFilterData(filtertype);
        String validCategoryData = boardValidator.DefaultCategoryData(category);
        Long validLimitCount = boardValidator.DefaultLimitCount(limitcount);

        List<BoardAllResponseDto> bordResponseDtos = boardService.getSortedBoard(validFilterData, validCategoryData, validmapdata, validLimitCount);
        return new ResponseEntity<>(new BoardList("success", "리스트 " + filtertype + " 정렬 성공", bordResponseDtos), HttpStatus.OK);
    }

    //**게시글 상세 페이지 불러오기
    @GetMapping("/board/{boardid}")
    public ResponseEntity<BoardDetail> getDetailBoard(@PathVariable Long boardid, @RequestParam(value = "userid", required = false) Optional<Long> userid, @AuthenticationPrincipal User user) {
        // isliked가 null일때는 로그인을 하지 않은 유저이므로 찜하기 부분을 False로 처리한다.(로그인 안했을 때는 찜 그냥 false)
        Boolean isliked = boardValidator.DefaultLiked(userid, boardid, user);

        // userService boardid, isliked
        BoardDetailResponseDto boardDetailResponseDto = boardService.getDetailBoard(boardid, isliked);
        return new ResponseEntity<>(new BoardDetail("success", "게시글 상세 불러오기 성공", boardDetailResponseDto), HttpStatus.OK);

    }

    //직접 사용자 검색 기능
    @GetMapping("/search")
    public ResponseEntity<BoardList> getSearchBoardList(@RequestParam(value = "limit", required = false) Long limitcount, @RequestParam(value = "filtertype", required = false) Optional<String> filtertype, @RequestParam(value = "searchtitle", required = false) Optional<String> searchtitle, @RequestParam(value = "mapdata", required = false) Optional<String> mapdata) {
        // 각각의 변수에 대한 default값 설정
        String validmapdata = boardValidator.DefaultMapData(mapdata);
        String validFilterData = boardValidator.DefaultFilterData(filtertype);
        String validSearchData = boardValidator.DefaultSearchData(searchtitle);
        Long validLimitCount = boardValidator.DefaultLimitCount(limitcount);
        System.out.println(validmapdata + validFilterData + validSearchData);

        List<BoardAllResponseDto> boardResponseDtos = boardService.getSearchBoard(validFilterData, validSearchData, validmapdata, validLimitCount);
        return new ResponseEntity<>(new BoardList("success", "검색 " + validFilterData + " 성공", boardResponseDtos), HttpStatus.OK);
    }

    //릴스 동영상 get 하는 방식
    @GetMapping("/board/video")
    public ResponseEntity<AllVideo> getVideo(@RequestParam(value = "limit", required = false) Long limitcount) {
        Long validLimitCount = boardValidator.DefaultLimitCount(limitcount);
        List<VideoAllResponseDto> videoAllResponseDtos = boardService.getAllVideo(validLimitCount);
        return new ResponseEntity<>(new AllVideo("success", "영상 전송 성공", videoAllResponseDtos), HttpStatus.OK);
    }
}
