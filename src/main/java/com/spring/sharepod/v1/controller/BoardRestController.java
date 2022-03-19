package com.spring.sharepod.v1.controller;


import com.spring.sharepod.entity.User;
import com.spring.sharepod.model.AllVideo;
import com.spring.sharepod.model.BoardDetail;
import com.spring.sharepod.model.BoardList;
import com.spring.sharepod.v1.dto.request.BoardRequestDto;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.dto.response.BoardResponseDto;
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
    public ResponseEntity<AllVideo> getVideo(@RequestParam(value = "limit", required=false) Long limitCount) {
        //limit 안 들어오면 5로 고정
        Long validLimitCount = boardValidator.DefaultLimitCount(limitCount);

        //
        List<BoardResponseDto.VideoAll> videoAllResponseDtos = boardService.getAllVideo(validLimitCount);
        return new ResponseEntity<>(new AllVideo("success", "영상 전송 성공", videoAllResponseDtos), HttpStatus.OK);
    }

    //** 9번 게시판 작성 (구현 완료)
    @PostMapping("/board")
    public BasicResponseDTO writeBoard(@RequestPart BoardRequestDto.WriteBoard boardWriteRequestDTO,
                                       @RequestPart MultipartFile[] imgFiles,
                                       @RequestPart MultipartFile videoFile, @AuthenticationPrincipal User user) throws IOException {

        //token과 boardWriteRequestDto의 userid와 비교
        tokenValidator.userIdCompareToken(boardWriteRequestDTO.getUserId(), user.getId());


        //게시판 업로드
        BoardRequestDto.WriteBoard writeBoard = awsS3Service.boardUpload(boardWriteRequestDTO,imgFiles,videoFile);

        //DB저장 및 리턴
        return boardService.wirteBoard(writeBoard);
    }

    //** 10번 게시글 상세 페이지 불러오기 (구현 완료)
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BoardDetail> getDetailBoard(@PathVariable Long boardId, @RequestParam(value = "userid", required=false) Optional<Long> userId, @AuthenticationPrincipal User user) {
        // isliked가 null일때는 로그인을 하지 않은 유저이므로 찜하기 부분을 False로 처리한다.(로그인 안했을 때는 찜 그냥 false)
        Boolean islLked = boardValidator.DefaultLiked(userId,boardId,user);

        BoardResponseDto.BoardDetail boardDetailResponseDto = boardService.getDetailBoard(boardId, islLked);
        return new ResponseEntity<>(new BoardDetail("success", "게시글 상세 불러오기 성공", boardDetailResponseDto), HttpStatus.OK);

    }

    //** 11번 게시판 수정 (구현 완료)
    @PatchMapping("/board/{boardId}")
    public BasicResponseDTO updateDetailBoard(@PathVariable Long boardId, @RequestPart BoardRequestDto.PatchBoard patchRequestDTO, @AuthenticationPrincipal User user,@RequestPart MultipartFile[] imgFiles,
                                              @RequestPart MultipartFile videoFile)throws IOException {
        //token과 patchRequestDTO의 userid와 비교
        tokenValidator.userIdCompareToken(patchRequestDTO.getUserId(), user.getId());

        BoardRequestDto.PatchBoard boardPatchRequestDTOadd = awsS3Service.boardUpdate(boardId, patchRequestDTO, imgFiles, videoFile);

        //게시판 수정 업로드
        return boardService.updateBoard(boardId, boardPatchRequestDTOadd);
    }

    //**12번 게시판 삭제 (구현 완료)
    @DeleteMapping("/board/{boardId}")
    public BasicResponseDTO deleteBoard(@PathVariable Long boardId, @RequestBody Map<String, Long> user, @AuthenticationPrincipal User tokenUser) {
        //token과 user.get("userid")와 비교
        tokenValidator.userIdCompareToken(user.get("userId"),tokenUser.getId());

        return boardService.deleteboard(boardId, user.get("userId"));
    }

    //13번 메인 전체 상품 최신순 보여주기 (구현 완료)
    @GetMapping("/board")
    public ResponseEntity<BoardList> getBoardList(@RequestParam(value = "limit", required=false) Long limitCount) {
        //Long 값이 들어오지 않는다면 5개로 고정한다.
        Long validLimitCount = boardValidator.DefaultLimitCount(limitCount);

        List<BoardResponseDto.BoardAll> boardResponseDtos = boardService.getAllBoard(validLimitCount);
        return new ResponseEntity<>(new BoardList("success", "리스트 최신순 성공", boardResponseDtos), HttpStatus.OK);
    }


    //14번 상품 카테고리 클릭 시, 상세 리스트 페이지로 이동 (구현 완료)
    @GetMapping("/board/sort")
    public ResponseEntity<BoardList> getCategoryBoardList(@RequestParam(value = "limit", required=false) Long limitcount, @RequestParam(value = "filterType", required=false) Optional<String> filtertype, @RequestParam(value = "category", required=false) Optional<String> category, @RequestParam(value = "boardRegion", required=false) Optional<String> mapdata) {
        // 각각의 변수에 대한 default값 설정
        String validmapdata = boardValidator.DefaultMapData(mapdata);
        String validFilterData = boardValidator.DefaultFilterData(filtertype);
        String validCategoryData = boardValidator.DefaultCategoryData(category);
        Long validLimitCount = boardValidator.DefaultLimitCount(limitcount);

        List<BoardResponseDto.BoardAll> bordResponseDtos = boardService.getSortedBoard(validFilterData, validCategoryData, validmapdata, validLimitCount);
        return new ResponseEntity<>(new BoardList("success", "리스트 " + validFilterData + " 정렬 성공", bordResponseDtos), HttpStatus.OK);
    }



    //15번 직접 사용자 검색 기능 (구현 완료)
    @GetMapping("/search")
    public ResponseEntity<BoardList> getSearchBoardList(@RequestParam(value = "limit", required=false) Long limitcount, @RequestParam(value = "filtertype", required=false) Optional<String> filtertype, @RequestParam(value = "searchTitle", required=false) Optional<String> searchtitle, @RequestParam(value = "boardRegion", required=false ) Optional<String> boardRegion) {
        // 각각의 변수에 대한 default값 설정
        String validBoardRegion = boardValidator.DefaultMapData(boardRegion);
        String validFilterData = boardValidator.DefaultFilterData(filtertype);
        String validSearchData = boardValidator.DefaultSearchData(searchtitle);
        Long validLimitCount = boardValidator.DefaultLimitCount(limitcount);
        System.out.println(validBoardRegion+validFilterData+validSearchData);

        List<BoardResponseDto.BoardAll> boardResponseDtos = boardService.getSearchBoard(validFilterData, validSearchData, validBoardRegion, validLimitCount);
        return new ResponseEntity<>(new BoardList("success", "검색 " + validFilterData + " 성공", boardResponseDtos), HttpStatus.OK);
    }


}
