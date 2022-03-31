package com.spring.sharepod.v1.service;

import com.spring.sharepod.entity.Amount;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.ImgFiles;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.v1.dto.request.BoardRequestDto;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.dto.response.Board.*;
import com.spring.sharepod.v1.dto.response.VideoAllResponseDto;
import com.spring.sharepod.v1.repository.AmountRepository;
import com.spring.sharepod.v1.repository.Board.BoardRepository;
import com.spring.sharepod.v1.repository.ImgFilesRepository;
import com.spring.sharepod.v1.repository.SearchForm;
import com.spring.sharepod.v1.repository.UserRepository;
import com.spring.sharepod.v1.validator.BoardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

import static com.spring.sharepod.exception.CommonError.ErrorCode.BOARD_NOT_EQUAL_WRITER;
import static com.spring.sharepod.exception.CommonError.ErrorCode.BOARD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final AwsS3Service awsS3Service;

    private final BoardValidator boardValidator;
    private final ImgFilesRepository imgFilesRepository;

    private final AmountRepository amountRepository;
    private final EntityManager entityManager;


    //8번 API 릴스 video 전체 GET(Limit) (구현 완료)
    @Transactional
    public List<VideoAllResponseDto> getAllVideo(Long startNum) {

        //TypedQuery<VideoAllResponseDto> query = entityManager.createQuery("SELECT NEW com.spring.sharepod.v1.dto.response.VideoAllResponseDto(b.id,i.videoUrl,u.userImg,u.nickName)  FROM Board b INNER JOIN b.imgFiles as i on b.id=i.board.id INNER JOIN b.user as u on b.user = u order by b.modifiedAt", VideoAllResponseDto.class);
        //Long randomIdx = Math.random()/query
        //query.setFirstResult()
        //query.setFirstResult(startNum);
        //query.setMaxResults(3);
        //List<VideoAllResponseDto> resultList = query.getResultList();

       List<VideoAllResponseDto> videoAllResponseDtoList = boardRepository.videoAll(startNum);

//        String q ="생략";
//        JpaResultMapper result = new JpaResultMapper();
//        Query query = entityManager.createQuery(q);
//        List<VideoAllResponseDto> list = result.list(query,VideoAllResponseDto.class);
        //List<VideoAllResponseDto> resultList = this.

        //모든 릴스 가져오기
        //List<Board> boardList = boardRepository.findAllByVideoUrlRan();

//        // 릴스를 반환해서 저장할 리스트
//        List<BoardResponseDto.VideoAll> videoAllResponseDtos = new ArrayList<>();
//
//        // 릴스 덩어리 해쳐서 넣어주기
//        for (Board board : boardList) {
//            String nickname = board.getUser().getNickName();
//            String userimg = board.getUser().getUserImg();
//
//            // videoAllResponseDto 생성
//            BoardResponseDto.VideoAll videoAllResponseDto = BoardResponseDto.VideoAll.builder()
//                    .boardId(board.getId())
//                    .videoUrl(board.getImgFiles().getVideoUrl())
//                    .userImg(userimg)
//                    .nickName(nickname)
//                    .build();
//
//            // 반환할 리스트에 저장하기
//            videoAllResponseDtos.add(videoAllResponseDto);
//        }

        return videoAllResponseDtoList;
    }

    //9번 API 게시판 작성 (구현 완료)
    @Transactional
    public BoardResponseDto.BoardWrite wirteBoard(BoardRequestDto.WriteBoard writeBoard) {

        //작성자의 id로 user를 찾는다.
        User user = userRepository.findById(writeBoard.getUserId()).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.USER_NOT_FOUND));

        //보드 저장
        Long boardId = boardRepository.save(Board.builder()
                .user(user)
                .title(writeBoard.getTitle())
                .contents(writeBoard.getContents())
                .boardRegion(writeBoard.getBoardRegion())
                .category(writeBoard.getCategory())
                .productQuality(writeBoard.getProductQuality())
                .boardTag(writeBoard.getBoardTag())
                .mainAppear(true)
                .build()).getId();

        //보드를 저장한 Id를 통해 imgfiles와 amount를 저장한다.
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ErrorCodeException(BOARD_NOT_FOUND));

        imgFilesRepository.save(ImgFiles.builder()
                .firstImgUrl(writeBoard.getFirstImgUrl())
                .secondImgUrl(writeBoard.getSecondImgUrl())
                .lastImgUrl(writeBoard.getLastImgUrl())
                .videoUrl(writeBoard.getVideoUrl())
                .board(board)
                .build());

        amountRepository.save(Amount.builder()
                .dailyRentalFee(writeBoard.getDailyRentalFee())
                .originPrice(writeBoard.getOriginPrice())
                .board(board)
                .build());

        return BoardResponseDto.BoardWrite.builder()
                .result("success")
                .msg(user.getNickName() + "님의 게시글 작성 완료되었습니다.")
                .userId(writeBoard.getUserId())
                .boardData(BoardResponseDto.BoardData.builder()
                        .originalPrice(writeBoard.getOriginPrice())
                        .contents(writeBoard.getContents())
                        .productQuality(writeBoard.getProductQuality())
                        .boardId(board.getId())
                        .firstImgUrl(writeBoard.getFirstImgUrl())
                        .title(writeBoard.getTitle())
                        .boardRegion(writeBoard.getBoardRegion())
                        .dailyRentalFee(writeBoard.getDailyRentalFee())
                        .boardTag(writeBoard.getBoardTag())
                        .category(writeBoard.getCategory())
                        .modifiedAt(board.getModifiedAt())
                        .build())
                .build();
    }

    // 10번 API 상세 페이지 board GET (구현 완료)
    @Transactional
    public BoardDetail getDetailBoard(Long boardId, Optional<Long> userId) {

        Boolean isLiked = boardValidator.DefaultLiked(userId, boardId);

        TypedQuery<BoardDetails> query = entityManager.createQuery("SELECT NEW com.spring.sharepod.v1.dto.response.Board.BoardDetails(i.firstImgUrl,i.secondImgUrl,i.lastImgUrl,i.videoUrl,b.title,b.contents,ba.originPrice,ba.dailyRentalFee,b.boardTag,b.user.nickName,b.user.userRegion,b.boardRegion,b.category,b.productQuality,b.likeNumber.size,b.user.userImg,b.modifiedAt) FROM Board b INNER JOIN b.imgFiles as i on b.id=i.board.id INNER JOIN b.amount ba on i.board.id=ba.board.id where b.id=:boardId", BoardDetails.class);
        //query.setParameter("isLiked",isLiked);
        query.setParameter("boardId",boardId);
        BoardDetails resultList = query.getSingleResult();

        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(resultList.getFirstImgUrl());
        fileNameList.add(resultList.getSecondImgUrl());
        fileNameList.add(resultList.getLastImgUrl());
        fileNameList.removeAll(Arrays.asList("", null));

        // 존재한다면 받아온 내용들을 담아서 보내주기
        BoardResponseDto.BoardDetail boardDetailResponseDto = BoardResponseDto.BoardDetail.builder()
                .Title(resultList.getTitle())
                .videoUrl(resultList.getVideoUrl())
                .imgFiles(fileNameList)
                .contents(resultList.getContents())
                .originPrice(resultList.getOriginPrice())
                .dailyRentalFee(resultList.getDailyRentalFee())
                .boardTag(resultList.getBoardTag())
                .nickName(resultList.getNickName())
                .sellerRegion(resultList.getUserRegion())
                .boardRegion(resultList.getBoardRegion())
                .category(resultList.getCategory())
                .boardQuaility(resultList.getProductQuality())
                .isLiked(isLiked)
                .likeCount(resultList.getLikeNumberSize())
                .sellerImg(resultList.getUserImg())
                .modifiedAt(resultList.getModifiedAt())
                .build();

//        //보드가 존재하지 않을 시 메시지 호출
//        Board board = boardValidator.ValidByBoardId(boardId);
//        List<String> fileNameList = new ArrayList<>();
//        fileNameList.add(board.getImgFiles().getFirstImgUrl());
//        fileNameList.add(board.getImgFiles().getSecondImgUrl());
//        fileNameList.add(board.getImgFiles().getLastImgUrl());
//        fileNameList.removeAll(Arrays.asList("", null));
//
//        // 존재한다면 받아온 내용들을 담아서 보내주기
//        BoardResponseDto.BoardDetail boardDetailResponseDto = BoardResponseDto.BoardDetail.builder()
//                .Title(board.getTitle())
//                .videoUrl(board.getImgFiles().getVideoUrl())
//                .imgFiles(fileNameList)
//                .contents(board.getContents())
//                .originPrice(board.getAmount().getOriginPrice())
//                .dailyRentalFee(board.getAmount().getDailyRentalFee())
//                .boardTag(board.getBoardTag())
//                .nickName(board.getUser().getNickName())
//                .sellerRegion(board.getUser().getUserRegion())
//                .boardRegion(board.getBoardRegion())
//                .category(board.getCategory())
//                .boardQuaility(board.getProductQuality())
//                .isLiked(isLiked)
//                .likeCount(board.getLikeNumber().size())
//                .sellerImg(board.getUser().getUserImg())
//                .modifiedAt(board.getModifiedAt())
//                .build();

        return BoardDetail.builder()
                .result("success")
                .msg("상세 페이지 조회 성공")
                .data(boardDetailResponseDto)
                .build();
    }


    //11번 API 게시판 수정 (구현 완료)
    @Transactional
    public BoardResponseDto.BoardWrite updateBoard(Long boardId, BoardRequestDto.PatchBoard patchRequestDTO) {

        //수정할 게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );

        //받아온 userid와 boardid의 작성자가 다를때
        if (!Objects.equals(patchRequestDTO.getUserId(), board.getUser().getId())) {
            throw new ErrorCodeException(BOARD_NOT_EQUAL_WRITER);
        }

        //게시판 업데이트
        board.getImgFiles().updateImgFiles(patchRequestDTO.getFirstImgUrl(), patchRequestDTO.getSecondImgUrl(), patchRequestDTO.getLastImgUrl(), patchRequestDTO.getVideoUrl());
        board.getAmount().updateAmount(patchRequestDTO.getOriginPrice(), patchRequestDTO.getDailyRentalFee());
        board.updateBoard(patchRequestDTO.getTitle(), patchRequestDTO.getContents(), patchRequestDTO.getCategory(), patchRequestDTO.getBoardRegion(), patchRequestDTO.getProductQuality(), patchRequestDTO.getBoardTag());

        //수정된 게시판 boardid로 검색해 가져오기
        Board boardupdate = boardRepository.findById(boardId).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.BOARD_NOT_FOUND)
        );

        return BoardResponseDto.BoardWrite.builder()
                .result("success")
                .msg(board.getUser().getNickName() + "님의 게시글 수정 완료되었습니다.")
                .userId(patchRequestDTO.getUserId())
                .boardData(BoardResponseDto.BoardData.builder()
                        .contents(board.getContents())
                        .originalPrice(board.getAmount().getOriginPrice())
                        .productQuality(board.getProductQuality())
                        .boardId(boardupdate.getId())
                        .firstImgUrl(patchRequestDTO.getFirstImgUrl())
                        .title(patchRequestDTO.getTitle())
                        .boardRegion(patchRequestDTO.getBoardRegion())
                        .dailyRentalFee(patchRequestDTO.getDailyRentalFee())
                        .boardTag(patchRequestDTO.getBoardTag())
                        .category(patchRequestDTO.getCategory())
                        .modifiedAt(boardupdate.getModifiedAt())
                        .build())
                .build();
    }

    @Transactional
    public BoardResponseDto.BoardModifiedData getModifiedBoard(Long boardId){
        TypedQuery<BoardModifedDetail> query = entityManager.createQuery("SELECT NEW com.spring.sharepod.v1.dto.response.Board.BoardModifedDetail(i.firstImgUrl,i.secondImgUrl,i.lastImgUrl,i.videoUrl,b.title,b.contents,ba.originPrice,ba.dailyRentalFee,b.boardTag,b.boardRegion,b.category,b.productQuality,b.modifiedAt) FROM Board b INNER JOIN b.imgFiles as i on b.id=i.board.id INNER JOIN b.amount ba on i.board.id=ba.board.id where b.id=:boardId", BoardModifedDetail.class);
        //query.setParameter("isLiked",isLiked);
        query.setParameter("boardId",boardId);
        BoardModifedDetail resultList = query.getSingleResult();


        return BoardResponseDto.BoardModifiedData.builder()
                .result("success")
                .msg(boardId + " 번 게시글 수정 전의 GET 데이터")
                .boardData(resultList)
                .build();

    }


    //12번 API 게시판 삭제 (구현 완료)
    @Transactional
    public BasicResponseDTO deleteboard(Long boardId, Long userId) {

        //삭제할 게시판 boardid로 검색해 가져오기
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ErrorCodeException(BOARD_NOT_FOUND)
        );



        //받아온 userid와 boardid의 작성자가 다를때
        if (!Objects.equals(userId, board.getUser().getId())) {
            throw new ErrorCodeException(BOARD_NOT_EQUAL_WRITER);
        }


        String firstImg = board.getImgFiles().getFirstImgUrl();
        String secondImg = board.getImgFiles().getSecondImgUrl();
        String lastImg = board.getImgFiles().getLastImgUrl();
        String videoUrl = board.getImgFiles().getVideoUrl();

        //DB에 존재하는 풀 길이의 Url을 받아와서 제거하기 위한 키를 만들어준다.
        if(board.getImgFiles().getSecondImgUrl() == null){

        }else{
            secondImg = secondImg.substring(secondImg.lastIndexOf("/") + 1);
        }

        if(board.getImgFiles().getLastImgUrl() == null){

        }else{
            lastImg = lastImg.substring(lastImg.lastIndexOf("/") + 1);
        }

        if(board.getImgFiles().getVideoUrl() == null){

        }else {
            videoUrl = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
        }



        firstImg = firstImg.substring(firstImg.lastIndexOf("/") + 1);

//        //리스트에 담에서 넣어주기
//        String[] imgs = {firstImg, secondImg, lastImg, videoUrl};
//
//        List<String> fileName = Arrays.asList(imgs);
//        fileName.removeAll(Collections.singletonList(null));

        List<String> fileNameList = new ArrayList<>();
        fileNameList.add(firstImg);
        fileNameList.add(secondImg);
        fileNameList.add(lastImg);
        fileNameList.add(videoUrl);
        fileNameList.removeAll(Arrays.asList("", null));


        awsS3Service.deleteBoardFiles(fileNameList);


        //게시글 삭제
        boardRepository.deleteById(boardId);

        return BasicResponseDTO.builder()
                .result("success")
                .msg(board.getUser().getNickName() + "번 게시글 삭제 완료되었습니다.")
                .build();
    }


    //API 13번 메인 페이지 전체 게시글 불러오기 (아예 완료)
    @Transactional
    public BoardResponseDto.BoardAllList getAllBoard(Optional<Long> userId) {
//        TypedQuery<BoardAllResponseDto> query = entityManager.createQuery("SELECT new com.spring.sharepod.v1.dto.response.Board.BoardAllResponseDto(b.id,b.imgFiles.firstImgUrl,b.title,b.category,b.amount.dailyRentalFee,b.boardRegion,b.boardTag,b.modifiedAt) FROM Board b", BoardAllResponseDto.class);
//        query.setMaxResults(8);
//        List<BoardAllResponseDto> boardlist = query.getResultList();
        //int resultCount = boardlist.size();

        Boolean isLiked = false;
        List<BoardAllResponseDto> querydslBoardList = boardRepository.searchAllBoard();

        int resultCount = querydslBoardList.size();
        for (int i = 0; i < resultCount; i++) {
            //System.out.println(querydslBoardList.get(i).getId() + "boardID");
            isLiked = boardValidator.DefaultLiked(userId,querydslBoardList.get(i).getId());
            querydslBoardList.get(i).setIsLiked(Optional.ofNullable(isLiked));
        }


        // 모든 게시글 가져오기
        //List<BoardAllResponseDto> boardList = boardRepository.findAllByOrderByModifiedAtDesc();
        BoardResponseDto.BoardAllList boardAllList = BoardResponseDto.BoardAllList.builder()
                .result("success")
                .msg("메인 페이지 게시글 반환 성공")
                .resultCount(resultCount)
                .listData(querydslBoardList)
                .build();

        return boardAllList;
    }


    //15번 카테고리 정렬별 보여주기 (구현 완료)
    @Transactional
    public BoardResponseDto.BoardAllList getSortedBoard(String filterType, String category, String boardRegion, int startNum, String searchTitle, Optional<Long> userId) {
        List<BoardAllResponseDto> boardList = new ArrayList<>();
//        System.out.println("filterType : " + filterType);
//        System.out.println("category : " + category);
//        System.out.println("boardRegion :" + boardRegion);
//        System.out.println("limitCount" + startNum);

        int boardLength = 0;
        Boolean isLiked = false;
        switch (filterType) {
            case "quality":
                boardList = boardRepository.searchFormQuality(SearchForm.builder()
                        .startNum(startNum)
                        .category(category)
                        .searchTitle(searchTitle)
                        .boardRegion(boardRegion).build());
                boardLength = boardList.size();
                break;

            case "cost":
                boardList = boardRepository.searchFormCost(SearchForm.builder()
                        .startNum(startNum)
                        .category(category)
                        .searchTitle(searchTitle)
                        .boardRegion(boardRegion).build());
                boardLength = boardList.size();
                break;

            default:
                boardList = boardRepository.searchFormRecent(SearchForm.builder()
                        .startNum(startNum)
                        .category(category)
                        .searchTitle(searchTitle)
                        .boardRegion(boardRegion).build());
                boardLength = boardList.size();
        }

        for (int i = 0; i < boardLength; i++) {
            isLiked = boardValidator.DefaultLiked(userId,boardList.get(i).getId());
            boardList.get(i).setIsLiked(Optional.ofNullable(isLiked));
        }

        BoardResponseDto.BoardAllList boardAllList = BoardResponseDto.BoardAllList.builder()
                .result("success")
                .msg("메인 페이지 게시글 반환 성공")
                .resultCount(boardLength)
                .listData(boardList)
                .build();

        return boardAllList;
    }


//    //15번 검색한 내용에 대한 정보 (구현 완료)
//    @Transactional
//    public List<BoardResponseDto.BoardAll> getSearchBoard(String filtertype, String searchtitle, String boardRegion, Long startCount, Optional<Long> userId) {
//        List<Board> boardList = new ArrayList<>();
//        int boardLength = 0;
//
//        switch (filtertype) {
//            case "quality":
//                boardList = boardRepository.findByAndMapAndSearchByQuality(boardRegion, searchtitle, startCount);
//                boardLength = boardRepository.findByAndMapAndSearchByQualityCount(boardRegion, searchtitle);
//                System.out.println(boardLength + "boardLength");
//                break;
//
//            case "cost":
//                boardList = boardRepository.findByAndMapAndSearchByCost(boardRegion, searchtitle, startCount);
//                break;
//
//            default:
//                boardList = boardRepository.findByAndMapAndSearchByCreatedAt(boardRegion, searchtitle, startCount);
//
//        }
//        return getBoardService(boardList, userId);
//    }


    //////////////////반복되는 로직을 처리해주는 함수들
//    public BoardResponseDto.BoardAllList getBoardService(List<BoardResponseDto.BoardAll> boardList, int resultCount, Optional<Long> userId) {
//
//
//        // 게시글을 반환해서 저장할 리스트
//        List<BoardResponseDto.BoardAll> boardResponseDtos = new ArrayList<>();
//
//        // 게시글 해쳐서 for문을 통해 하나씩 넣어주기
//        for (Board board : boardList) {
//            Long boardId = board.getId();
//
//            //로그인을 했을 경우에는 좋아요 보이도록
//            Boolean isLiked = boardValidator.DefaultLiked(userId, boardId);
//
//            // BoardResponseDto 생성
//            BoardResponseDto.BoardAll boardResponseDto = BoardResponseDto.BoardAll.builder()
//                    .boardId(board.getId())
//                    .category(board.getCategory())
//                    .title(board.getTitle())
//                    .firstImgUrl(board.getImgFiles().getFirstImgUrl())
//                    .dailyRentalFee(board.getAmount().getDailyRentalFee())
//                    .boardRegion(board.getBoardRegion())
//                    .boardTag(board.getBoardTag())
//                    .isLiked(isLiked)
//                    .category(board.getCategory())
//                    .modifiedAt(board.getModifiedAt())
//                    .build();
//
//            // 반환할 리스트에 저장하기
//            boardResponseDtos.add(boardResponseDto);
//        }
//
//        return BoardResponseDto.BoardAllList.builder()
//                .result("success")
//                .msg("게시글 반환 성공")
//                .resultCount(resultCount)
//                .listData(boardResponseDtos)
//                .build();
//    }

}
