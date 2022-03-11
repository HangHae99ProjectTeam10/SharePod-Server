package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.dto.response.Board.RentBuyerResponseDto;
import com.spring.sharepod.dto.response.Board.RentSellerResponseDto;
import com.spring.sharepod.dto.response.Liked.LikedResponseDto;
import com.spring.sharepod.dto.response.UserInfoResponseDto;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.LikedRepository;
import com.spring.sharepod.repository.UserRepository;
import com.spring.sharepod.validator.RegisterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.spring.sharepod.exception.ErrorCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class UserService {

    private final RegisterValidator registerValidator;
    //    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LikedRepository likedRepository;
    private final BoardRepository boardRepository;

    // 회원가입
    @Transactional
    public Long createUser(UserRegisterRequestDto userRegisterRequestDto) {
        //회원가입 유효성 검사 validator 통해서 검증한다. 중간에 이상하거 있으면 바로 거기서 메시지 반환하도록
        registerValidator.validateUserRegisterData(userRegisterRequestDto);

        // 여기서부터는 검증된 데이터들이기에 그냥 비밀번호 암호화하고 빌더 패턴으로 유저에 대한 정보를 생성한다.
        // 비밀번호 암호화
        //String password = passwordEncoder.encode(userRegisterRequestDto.getPassword());

        // 유저 생성
        User user = User.builder()
                .userimg(userRegisterRequestDto.getUserimg())
                .username(userRegisterRequestDto.getUsername())
                .password(userRegisterRequestDto.getPassword())
                .mapdata(userRegisterRequestDto.getMapdata())
                .nickname(userRegisterRequestDto.getNickname())
                //.roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build();

        // 유저 저장하기
        userRepository.save(user);
        return user.getId();
    }

    //userinfo 불러오기
    @Transactional
    public UserInfoResponseDto getUserInfo(Long userid) {
        //로그인 되어 있는지 , 되어 있다면 위 userid가 존재하는지 알아야한다.

        User user = userRepository.findById(userid).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

        //build해서 찾은 user의 내용 중 일부를 responseDto를 통해서 전달한다.
        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
                .userid(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .mapdata(user.getMapdata())
                .userimg(user.getUserimg())
                .build();

        return userInfoResponseDto;
    }

    //찜목록 불러오기
    @Transactional
    public List<LikedResponseDto> getUserLikeBoard(Long userid){
        //해당하는 유저가 존재하는 like 테이블에서 boardid를 받아오고 그 boardid를 통해
        // boardtitle과 userid category를 찾아낸다.

        List<LikedResponseDto> likedResponseDtoList = new ArrayList<>();

        List<Liked> userlikeList = likedRepository.findByUserId(userid);
        for (Liked userliked : userlikeList){
            Board likedBoardList = likedRepository.findByBoardId(userliked.getBoard().getId());

            LikedResponseDto likedResponseDto = LikedResponseDto.builder()
                    .boardid(likedBoardList.getId())
                    .boardtitle(likedBoardList.getTitle())
                    .userid(likedBoardList.getUser().getId())
                    .category(likedBoardList.getCategory())
                    .build();

            likedResponseDtoList.add(likedResponseDto);
        }
        return likedResponseDtoList;
    }

    //내가 등록한 글 불러오기
    @Transactional
    public List<MyBoardResponseDto> getMyBoard(Long userid){
        // userid를 사용하여 board에서 있는 것들 다 찾아오고 그에 따른 내용들을 전달
        List<MyBoardResponseDto> myBoardResponseDtoList = new ArrayList<>();

        List<Board> boardList = boardRepository.findByUserId(userid);

        for (Board board : boardList){
            MyBoardResponseDto myBoardResponseDto = MyBoardResponseDto.builder()
                    .boardid(board.getId())
                    .boardtitle(board.getTitle())
                    .nickname(board.getUser().getNickname())
                    .category(board.getCategory())
                    .build();

            myBoardResponseDtoList.add(myBoardResponseDto);
        }
        return myBoardResponseDtoList;
    }

    //내가 대여한 목록 불러오기
    @Transactional
    public List<RentBuyerResponseDto> getBuyList(Long userid){


    }


    //내가 빌려준 목록 불러오기
    @Transactional
    public List<RentSellerResponseDto> getSellList(Long userid){

    }





}
