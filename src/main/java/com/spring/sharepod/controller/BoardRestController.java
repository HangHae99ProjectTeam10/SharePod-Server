package com.spring.sharepod.controller;


import com.spring.sharepod.dto.request.Board.BoardPatchRequestDTO;
import com.spring.sharepod.dto.request.Board.BoardWriteRequestDTO;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Board.BoardAllResponseDto;
import com.spring.sharepod.dto.response.Board.BoardDetailResponseDto;
import com.spring.sharepod.dto.response.Board.VideoAllResponseDto;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.model.AllVideo;
import com.spring.sharepod.model.BoardDetail;
import com.spring.sharepod.model.BoardList;
import com.spring.sharepod.repository.LikedRepository;
import com.spring.sharepod.service.BoardService;
import com.spring.sharepod.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final S3Service s3Service;

    //게시판 작성
    @PostMapping("/board")
    public BasicResponseDTO writeBoard(@RequestPart BoardWriteRequestDTO boardWriteRequestDTO,
                                       @RequestPart MultipartFile[] imgfiles,
                                       @RequestPart MultipartFile videofile) throws IOException {
        //게시판 업로드
        BoardWriteRequestDTO boardWriteRequestDTOadd = s3Service.boardupload(boardWriteRequestDTO,imgfiles,videofile);

        //DB저장 및 리턴
        return boardService.wirteboard(boardWriteRequestDTOadd);
    }


    //게시판 생성




    //게시판 수정
    @PatchMapping("/board/{boardid}")
    public BasicResponseDTO updateboardcontroll(@PathVariable Long boardid, @RequestBody BoardPatchRequestDTO patchRequestDTO) {
        return boardService.updateboard(boardid, patchRequestDTO);
    }

    //게시판 삭제
    @DeleteMapping("/board/{boardid}")
    public BasicResponseDTO deleteboardcontroll(@PathVariable Long boardid, @RequestBody Map<String, Long> user) {
        return boardService.deleteboard(boardid, user.get("userid"));
    }

    //메인 전체 상품 최신순 보여주기
    @GetMapping("/board")
    public ResponseEntity<BoardList> getBoardList(@RequestParam(value = "limit") int limitcount) {
        List<BoardAllResponseDto> boardResponseDtos = boardService.getAllBoard(limitcount);
        return new ResponseEntity<>(new BoardList("success", "리스트 최신순 성공", boardResponseDtos), HttpStatus.OK);
    }


    //상품 카테고리 클릭 시, 상세 리스트 페이지로 이동
    @GetMapping("/board/sort")
    public ResponseEntity<BoardList> getSortedBoardList(@RequestParam(value = "limit") int limitcount, @RequestParam(value = "filtertype") String filtertype, @RequestParam(value = "category") String category, @RequestParam(value = "mapdata") String mapdata) {
        List<BoardAllResponseDto> bordResponseDtos = boardService.getSortedBoard(filtertype, category, mapdata, limitcount);
        return new ResponseEntity<>(new BoardList("success", "리스트 " + filtertype + " 정렬 성공", bordResponseDtos), HttpStatus.OK);
    }


    //게시글 상세 페이지 불러오기   (여기서 토큰이랑 받아온 userdata랑 일치해야함)
    @GetMapping("/board/{boardid}")
    public ResponseEntity<BoardDetail> getDetailBoard(@PathVariable Long boardid, @RequestParam(value = "userid") Optional<Long> userid) {
        Boolean isliked = null;
        if (!userid.isPresent()){
            isliked = false;
        }else{
            System.out.println(userid.get().longValue());
            Liked liked = likedRepository.findByLiked(boardid, userid.get().longValue());
             if (liked == null){
                isliked = false;
            }else{
                isliked = true;
            }
        }
        BoardDetailResponseDto boardDetailResponseDto = boardService.getDetailBoard(boardid, isliked);
        return new ResponseEntity<>(new BoardDetail("success", "게시글 상세 불러오기 성공", boardDetailResponseDto), HttpStatus.OK);

    }

    //직접 사용자 검색 기능
    @GetMapping("/search")
    public ResponseEntity<BoardList> getSearchBoardList(@RequestParam(value = "limit") int limitcount, @RequestParam(value = "filtertype") String filtertype, @RequestParam(value = "searchtitle") String searchtitle, @RequestParam(value = "mapdata") String mapdata) {
        List<BoardAllResponseDto> boardResponseDtos = boardService.getSearchBoard(filtertype, searchtitle, mapdata, limitcount);
        return new ResponseEntity<>(new BoardList("success", "검색 " + filtertype + " 성공", boardResponseDtos), HttpStatus.OK);
    }

    //릴스 동영상 get
    @GetMapping("/board/video")
    public ResponseEntity<AllVideo> getVideo(@RequestParam(value = "limit") int limitcount) {
        List<VideoAllResponseDto> videoAllResponseDtos = boardService.getAllVideo(limitcount);
        return new ResponseEntity<>(new AllVideo("success", "영상 전송 성공", videoAllResponseDtos), HttpStatus.OK);
    }
}
