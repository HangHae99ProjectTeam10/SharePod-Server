package com.spring.sharepod.v1.controller;


import com.spring.sharepod.entity.User;
import com.spring.sharepod.v1.dto.response.Board.AllVideo;
import com.spring.sharepod.v1.dto.response.Board.BoardDetail;
import com.spring.sharepod.v1.dto.request.BoardRequestDto;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.dto.response.Board.BoardResponseDto;
import com.spring.sharepod.v1.dto.response.VideoAllResponseDto;
import com.spring.sharepod.v1.service.AwsS3Service;
import com.spring.sharepod.v1.service.BoardService;
import com.spring.sharepod.v1.validator.BoardValidator;
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
    private final BoardValidator boardValidator;


    //8번 API 릴스 동영상 get 하는 방식 (구현 완료)
    @GetMapping("/board/video")
    public ResponseEntity<AllVideo> getVideo() {
        //limit 안 들어오면 5로 고정
        //Long validLimitCount = boardValidator.DefaultLimitCount(limitCount);

        List<VideoAllResponseDto> videoAllResponseDtos = boardService.getAllVideo();
        return new ResponseEntity<>(new AllVideo("success", "영상 전송 성공", videoAllResponseDtos), HttpStatus.OK);
    }

    //** 9번 게시판 작성 (구현 완료)
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

    //** 10번 게시글 상세 페이지 불러오기 (구현 완료)
    @GetMapping("/board/{boardId}")
    public BoardDetail getDetailBoard(@PathVariable Long boardId, @RequestParam(value = "userId", required = false) Optional<Long> userId) {
        // isliked가 null일때는 로그인을 하지 않은 유저이므로 찜하기 부분을 False로 처리한다.(로그인 안했을 때는 찜 그냥 false)

        //BoardResponseDto.BoardDetail boardDetailResponseDto = boardService.getDetailBoard(boardId,userId);
        return boardService.getDetailBoard(boardId,userId);

    }

    //** 11번 게시판 수정 (구현 완료)
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

    //** 11번 게시판 수정 정보 받아오기(구현 완료)
    @GetMapping("/board/modified/{userId}/{boardId}")
    public BoardResponseDto.BoardModifiedData updateDetailBoard(@PathVariable Long boardId, @PathVariable Long userId, @AuthenticationPrincipal User user){
        //token과 patchRequestDTO의 userid와 비교
        tokenValidator.userIdCompareToken(userId, user.getId());

        //게시판 수정 업로드
        return boardService.getModifiedBoard(boardId);
    }

    //**12번 게시판 삭제 (구현 완료)
    @DeleteMapping("/board/{boardId}")
    public BasicResponseDTO deleteBoard(@PathVariable Long boardId, @RequestBody Map<String, Long> user, @AuthenticationPrincipal User tokenUser) {
        //token과 user.get("userid")와 비교
        tokenValidator.userIdCompareToken(user.get("userId"), tokenUser.getId());
        return boardService.deleteboard(boardId, user.get("userId"));
    }

    //13번 메인 전체 상품 최신순 보여주기 (구현 완료) 8개만
    @GetMapping("/board")
    public BoardResponseDto.BoardAllList getBoardList(@RequestParam(value = "userId", required = false) Optional<Long> userId, @AuthenticationPrincipal Optional<User> user) {
//        if(user != null){
//            for(int i=0;i<user.get().getUserLikedList().size();i++){
//                List<Long> userLikedList = new ArrayList<>();
//                userLikedList.add(user.get().getUserLikedList().get(i).getBoard().getId());
//            }
//
//        }
        return boardService.getAllBoard(userId);
    }


    //14번 상품 카테고리 클릭 시, 상세 리스트 페이지로 이동 (구현 완료)
    @GetMapping("/board/sort")
    public BoardResponseDto.BoardAllList getCategoryBoardList(@RequestParam(value = "startNum", defaultValue = "0") int startNum, @RequestParam(value = "filterType", defaultValue = "recent") String filtertype, @RequestParam(value = "category") String category, @RequestParam(value = "boardRegion") String boardRegion, @RequestParam(value = "searchTitle") String searchtitle, @RequestParam(value = "userId", required = false) Optional<Long> userId) {
        return boardService.getSortedBoard(filtertype, category, boardRegion, startNum, searchtitle, userId);
    }


    //15번 직접 사용자 검색 기능 (구현 완료)
//    @GetMapping("/search")
//    public ResponseEntity<BoardList> getSearchBoardList(@RequestParam(value = "pageNum", defaultValue = "0") Long pageNum, @RequestParam(value = "filtertype", defaultValue = "recent") String filtertype, @RequestParam(value = "searchTitle", defaultValue = " ") String searchtitle, @RequestParam(value = "boardRegion", defaultValue = "All") String boardRegion, @RequestParam(value = "userId", required=false) Optional<Long> userId) {
//        List<BoardResponseDto.BoardAll> boardResponseDtos = boardService.getSearchBoard(filtertype, searchtitle, boardRegion, pageNum , userId);
//        //int boardCount = boardService.getSearchBoardCount(filtertype,searchtitle,boardRegion);
//        return new ResponseEntity<>(new BoardList("success", "검색 " + filtertype + " 성공", boardResponseDtos), HttpStatus.OK);
//    }


}
