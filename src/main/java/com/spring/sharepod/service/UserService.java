package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.User.UserModifyRequestDTO;
import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.dto.response.Board.RentBuyerResponseDto;
import com.spring.sharepod.dto.response.Board.RentSellerResponseDto;
import com.spring.sharepod.dto.response.Liked.LikedResponseDto;
import com.spring.sharepod.dto.response.UserInfoResponseDto;
import com.spring.sharepod.entity.Auth;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.ErrorCode;
import com.spring.sharepod.exception.ErrorCodeException;
import com.spring.sharepod.repository.AuthRepository;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.LikedRepository;
import com.spring.sharepod.repository.UserRepository;
import com.spring.sharepod.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.spring.sharepod.exception.ErrorCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidator userValidator;
    //    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LikedRepository likedRepository;
    private final BoardRepository boardRepository;
    private final AuthRepository authRepository;

    // 회원가입
    @Transactional
    public Long createUser(UserRegisterRequestDto userRegisterRequestDto) {
        //회원가입 유효성 검사 validator 통해서 검증한다. 중간에 이상하거 있으면 바로 거기서 메시지 반환하도록
        userValidator.validateUserRegisterData(userRegisterRequestDto);

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
        for (int i = 0; i<userlikeList.size();i++){
            //Board likedBoardList = likedRepository.findByBoardId(userliked.getBoard().getId());

            LikedResponseDto likedResponseDto = LikedResponseDto.builder()
                    .boardid(userlikeList.get(i).getBoard().getId())
                    .boardtitle(userlikeList.get(i).getBoard().getTitle())
                    .userid(userlikeList.get(i).getUser().getId())
                    .category(userlikeList.get(i).getBoard().getCategory())
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
    public List<RentBuyerResponseDto> getBuyList(Long userid) {
        List<RentBuyerResponseDto> rentBuyerResponseDtoList = new ArrayList<>();

        List<Auth> authList = authRepository.findByBuyerId(userid);

        for (int i = 0; i < authList.size(); i++) {
            RentBuyerResponseDto rentBuyerResponseDto = RentBuyerResponseDto.builder()
                    .boardid(authList.get(i).getBoard().getId())
                    .boardtitle(authList.get(i).getBoard().getTitle())
                    .nickname(authList.get(i).getAuthseller().getNickname())
                    .authid(authList.get(i).getId())
                    .category(authList.get(i).getBoard().getCategory())
                    .build();
            rentBuyerResponseDtoList.add(rentBuyerResponseDto);
        }
        return rentBuyerResponseDtoList;

    }


    //내가 빌려준 목록 불러오기
    @Transactional
    public List<RentSellerResponseDto> getSellList(Long userid){
        List<RentSellerResponseDto> rentSellerResponseDtoList = new ArrayList<>();

        List<Auth> authList = authRepository.findBySellerId(userid);

        for (int i = 0; i < authList.size(); i++) {
            RentSellerResponseDto rentSellerResponseDto = RentSellerResponseDto.builder()
                    .boardid(authList.get(i).getBoard().getId())
                    .boardtitle(authList.get(i).getBoard().getTitle())
                    .nickname(authList.get(i).getAuthbuyer().getNickname())
                    .authid(authList.get(i).getId())
                    .category(authList.get(i).getBoard().getCategory())
                    .build();
            rentSellerResponseDtoList.add(rentSellerResponseDto);
        }
        return rentSellerResponseDtoList;

    }


    @Transactional
    public String UserDelete(Long userid){
        //여기서 userid랑 토큰 비교
        // 다를 경우 에러 메시지 호출

        User user = userRepository.findById(userid).orElseThrow(()-> new ErrorCodeException(USER_NOT_FOUND));
        userRepository.deleteById(userid);

        return user.getNickname();
}

    //회원 정보 수정
    @Transactional
    public BasicResponseDTO usermodifyService(Long userid, UserModifyRequestDTO modifyRequestDTO){
        User user = userRepository.findById(userid).orElseThrow(
                () -> new ErrorCodeException(ErrorCode.LOGIN_USER_NOT_FOUND));

        //유저 이미지가 변경 되었을 때
        if(modifyRequestDTO.getUserimg() != null){
            user.update1(modifyRequestDTO);
        }
        // 아닐때
        else {
            user.update2(modifyRequestDTO);
        }

        return BasicResponseDTO.builder()
                .result("success")
                .msg("수정 성공")
                .build();

    }
}
