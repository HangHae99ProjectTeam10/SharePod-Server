package com.spring.sharepod.v1.controller;


import com.spring.sharepod.entity.User;
import com.spring.sharepod.v1.dto.request.BoardRequestDto;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.dto.response.Board.AllVideo;
import com.spring.sharepod.v1.dto.response.Board.BoardDetail;
import com.spring.sharepod.v1.dto.response.Board.BoardResponseDto;
import com.spring.sharepod.v1.dto.response.VideoAllResponseDto;
import com.spring.sharepod.v1.service.AwsS3Service;
import com.spring.sharepod.v1.service.BoardService;
import com.spring.sharepod.v1.validator.TokenValidator;
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
    private final AwsS3Service awsS3Service;
    private final TokenValidator tokenValidator;


    //8번 API 릴스 동영상 get 하는 방식
    //stress_test
    @GetMapping("/board/video")
    public ResponseEntity<AllVideo> getVideo(@RequestParam(value = "startNum", defaultValue = "0") Long startNum) {
        //limit 안 들어오면 5로 고정

        List<VideoAllResponseDto> videoAllResponseDtos = boardService.getAllVideo(startNum);
        return new ResponseEntity<>(new AllVideo("success", "영상 전송 성공", videoAllResponseDtos), HttpStatus.OK);
    }

    //** 9번 게시판 작성

    //유저에게 받아온 정보를 (BoardRequestDto.WriteBoard)를 통하여 컨트롤러를러에서 토큰과, RequestPart를 이용하여 비디오,이미지 파일을 받은 후
    //토큰을 비교하고나서 (awsS3Service.boardUpload)에서 파일들과 정보들을 저장하고, (boardService.wirteBoard)에서는 작성자의 user를 찾은 후
    //board의 정보들을 db에 저장하고 저장한 board id 를 통하여 imgfiles와 amount를 db에 저장한다.
    // 그후 서비스에서 (BoardResponseDto.BoardWrite)를 통해 저장 결과 값을 유저에게 보내준다.

    @PostMapping("/board")
    public BoardResponseDto.BoardWrite writeBoard(@RequestPart BoardRequestDto.WriteBoard boardWriteRequestDTO,
                                                  @RequestPart(required = false) MultipartFile[] imgFiles,
                                                  @RequestPart MultipartFile videoFile, @AuthenticationPrincipal User user) throws IOException {

        //token과 boardWriteRequestDto의 userid와 비교
        tokenValidator.userIdCompareToken(boardWriteRequestDTO.getUserId(), user.getId());


        //게시판 업로드
        BoardRequestDto.WriteBoard writeBoard = awsS3Service.boardUpload(boardWriteRequestDTO, imgFiles, videoFile);

        //DB저장 및 리턴
        return boardService.wirteBoard(writeBoard);
    }

    //** 10번 게시글 상세 페이지 불러오기
    // stress_test
    @GetMapping("/board/{boardId}")
    public BoardDetail getDetailBoard(@PathVariable Long boardId, @RequestParam(value = "userId", required = false) Optional<Long> userId) {
        // isliked가 null일때는 로그인을 하지 않은 유저이므로 찜하기 부분을 False로 처리한다.(로그인 안했을 때는 찜 그냥 false)

        return boardService.getDetailBoard(boardId,userId);

    }

    //** 11번 게시판 수정
    @PatchMapping("/board/{boardId}")
    public BoardResponseDto.BoardWrite updateDetailBoard(@PathVariable Long boardId,
                                                         @RequestPart BoardRequestDto.PatchBoard patchRequestDTO,
                                                         @AuthenticationPrincipal User user, @RequestPart(required = false)
                                                                 MultipartFile[] imgFiles,
                                                         @RequestPart MultipartFile videoFile) throws IOException {
        //token과 patchRequestDTO의 userid와 비교
        tokenValidator.userIdCompareToken(patchRequestDTO.getUserId(), user.getId());

        BoardRequestDto.PatchBoard boardPatchRequestDTOadd = awsS3Service.boardUpdate(boardId, patchRequestDTO, imgFiles, videoFile);

        //게시판 수정 업로드
        return boardService.updateBoard(boardId, boardPatchRequestDTOadd);
    }

    //** 11번 게시판 수정 정보 받아오기
    @GetMapping("/board/modified/{userId}/{boardId}")
    public BoardResponseDto.BoardModifiedData updateDetailBoard(@PathVariable Long boardId, @PathVariable Long userId, @AuthenticationPrincipal User user){
        //token과 patchRequestDTO의 userid와 비교
        tokenValidator.userIdCompareToken(userId, user.getId());

        //게시판 수정 업로드
        return boardService.getModifiedBoard(boardId);
    }

    //**12번 게시판 삭제
    @DeleteMapping("/board/{boardId}")
    public BasicResponseDTO deleteBoard(@PathVariable Long boardId, @RequestBody Map<String, Long> user, @AuthenticationPrincipal User tokenUser) {
        //token과 user.get("userid")와 비교
        tokenValidator.userIdCompareToken(user.get("userId"), tokenUser.getId());
        return boardService.deleteboard(boardId, user.get("userId"));
    }

    //13번 메인 전체 상품 최신순 보여주기 8개만
    // stress_test
    @GetMapping("/board")
    public BoardResponseDto.BoardAllList getBoardList(@RequestParam(value = "userId", required = false) Optional<Long> userId, @AuthenticationPrincipal Optional<User> user) {
        return boardService.getAllBoard(userId);
    }


    //14번 상품 카테고리 클릭 시, 상세 리스트 페이지로 이동
    // stress_test
    @GetMapping("/board/sort")
    public BoardResponseDto.BoardAllList getCategoryBoardList(@RequestParam(value = "startNum", defaultValue = "0") int startNum, @RequestParam(value = "filterType", defaultValue = "recent") String filtertype, @RequestParam(value = "category") String category, @RequestParam(value = "boardRegion") String boardRegion, @RequestParam(value = "searchTitle") String searchtitle, @RequestParam(value = "userId", required = false) Optional<Long> userId) {
        return boardService.getSortedBoard(filtertype, category, boardRegion, startNum, searchtitle, userId);
    }
}
