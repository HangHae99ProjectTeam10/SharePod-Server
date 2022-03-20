package com.spring.sharepod.v1.service;

import com.spring.sharepod.entity.Auth;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.jwt.JwtTokenProvider;
import com.spring.sharepod.model.LogOut;
import com.spring.sharepod.model.ReFreshToken;
import com.spring.sharepod.v1.dto.request.UserRequestDto;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.dto.response.BoardResponseDto;
import com.spring.sharepod.v1.dto.response.LikedResponseDto;
import com.spring.sharepod.v1.dto.response.UserResponseDto;
import com.spring.sharepod.v1.repository.AuthRepository;
import com.spring.sharepod.v1.repository.BoardRepository;
import com.spring.sharepod.v1.repository.LikedRepository;
import com.spring.sharepod.v1.repository.UserRepository;
import com.spring.sharepod.v1.validator.BoardValidator;
import com.spring.sharepod.v1.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.function.ServerRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.spring.sharepod.exception.CommonError.ErrorCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LikedRepository likedRepository;
    private final BoardRepository boardRepository;
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;
    private final AwsS3Service awsS3Service;
    private final BoardValidator boardValidator;


    //1번 API 로그인 구현 완료
    @Transactional
    public UserResponseDto.Login login(UserRequestDto.Login userLoginRequest, HttpServletResponse res) {
        //user를 username으로 찾고 없다면 메시지를 호출
        User user = userRepository.findByUsername(userLoginRequest.getUsername()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

        //비밀번호 다르면 메시지를 호출
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new ErrorCodeException(ErrorCode.PASSWORD_COINCIDE);
        }

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequest.toAuthentication();

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        UserResponseDto.LoginReFreshToken loginReFreshTokenResponseDto = jwtTokenProvider.generateToken(authentication, user.getId());

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), loginReFreshTokenResponseDto.getRefreshToken(), loginReFreshTokenResponseDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);


        res.addHeader("accessToken", loginReFreshTokenResponseDto.getAccessToken());
        res.addHeader("refreshToken", loginReFreshTokenResponseDto.getRefreshToken());

        return UserResponseDto.Login.builder()
                .result("success")
                .msg("로그인 성공")
                .userId(user.getId())
                .nickName(user.getNickName())
                .userRegion(user.getUserRegion())
                .userImg(user.getUserImg())
                .build();
    }

    //2번 API 리프레쉬 토큰 재발급
    public ResponseEntity<ReFreshToken> reissue(UserRequestDto.Reissue reissue, HttpServletResponse res, HttpServletRequest req) {
        System.out.println("reissue controller 1");

        System.out.println(reissue.getRefreshToken() + "refreshtoken 출력(request에서 받아온 내용)");
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken(), req)) {
            //fail로 리턴이 나올 경우 refresttoken 정보가 유효하지 않다고 보내면서 프론트가 다시 로그인 시킨다.
            return new ResponseEntity<>(new ReFreshToken("fail", "Refresh Token 정보가 유효하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        System.out.println("reissue controller 2");

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = redisTemplate.opsForValue().get("RT:" + authentication.getName());


        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if (ObjectUtils.isEmpty(refreshToken)) {
            return new ResponseEntity<>(new ReFreshToken("fail", "잘못된 요청입니다."), HttpStatus.BAD_REQUEST);
        }

        if (!refreshToken.equals(reissue.getRefreshToken())) {
            return new ResponseEntity<>(new ReFreshToken("fail", "ReFreshToken의 정보가 일치하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        UserResponseDto.LoginReFreshToken tokenInfo = jwtTokenProvider.generateToken(authentication, user.getId());



        HttpHeaders header = new HttpHeaders();
        header.set("accessToken", tokenInfo.getAccessToken());
        header.set("refreshToken", tokenInfo.getRefreshToken());


//
//        res.addHeader("accessToken", tokenInfo.getAccessToken());
//        res.addHeader("refreshToken", tokenInfo.getRefreshToken());

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return new ResponseEntity<>(new ReFreshToken("success", "accessToken 정보가 갱신되었습니다."), HttpStatus.OK);
    }

    //3번 API 로그아웃(구현 완료)
    public ResponseEntity<LogOut> logout(UserRequestDto.Reissue reIssueRequestDto, HttpServletRequest req) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(reIssueRequestDto.getAccessToken(), req)) {
            return new ResponseEntity<>(new LogOut("fail", "엑세스 토큰이 만료되었습니다. Reissue를 통해서 토큰을 재 발급 후 로그아웃으로 다시 와주세요."), HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reIssueRequestDto.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        } else {
            return new ResponseEntity<>(new LogOut("fail", "리프레쉬이 만료되었습니다.다시 로그인 해주세요."), HttpStatus.BAD_REQUEST);
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(reIssueRequestDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(reIssueRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return new ResponseEntity<>(new LogOut("success", "로그아웃 완료"), HttpStatus.OK);
    }


    //4번 API 회원가입 (구현 완료)
    @Transactional
    public String registerUser(UserRequestDto.Register userRegisterRequestDto) {
        //회원가입 유효성 검사 validator 통해서 검증한다. 중간에 이상하거 있으면 바로 거기서 메시지 반환하도록
        userValidator.validateUserRegisterData(userRegisterRequestDto);

        // 유저 생성
        User user = User.builder()
                .userImg(userRegisterRequestDto.getUserImg())
                .username(userRegisterRequestDto.getUsername())
                .password(passwordEncoder.encode(userRegisterRequestDto.getPassword()))
                .userRegion(userRegisterRequestDto.getUserRegion())
                .nickName(userRegisterRequestDto.getNickName())
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build();

        // 유저 저장하기
        userRepository.save(user);
        return user.getNickName();
    }

    //5번 API userinfo 불러오기 (구현 완료)
    @Transactional
    public UserResponseDto.UserInfo getUserInfo(Long userid) {
        User user = userValidator.ValidByUserId(userid);

        //build해서 찾은 user의 내용 중 일부를 responseDto를 통해서 전달한다.
        UserResponseDto.UserInfo userInfoResponseDto = UserResponseDto.UserInfo.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickName(user.getNickName())
                .userRegion(user.getUserRegion())
                .userImg(user.getUserImg())
                .build();

        return userInfoResponseDto;
    }

    //5번 API 찜목록 불러오기 (구현 완료)
    @Transactional
    public List<LikedResponseDto.Liked> getUserLikeBoard(Long userid) {
        //해당하는 유저가 존재하는 like 테이블에서 boardid를 받아오고 그 boardid를 통해
        // boardtitle과 userid category를 찾아낸다.
        List<LikedResponseDto.Liked> likedResponseDtoList = new ArrayList<>();

        // 없으면 for문 안돌고 빈 list가 들어간다.
        List<Liked> userlikeList = likedRepository.findByUserId(userid);

        for (Liked liked : userlikeList) {
            LikedResponseDto.Liked likedResponseDto = LikedResponseDto.Liked.builder()
                    .boardId(liked.getBoard().getId())
                    .boardTitle(liked.getBoard().getTitle())
                    .boardRegion(liked.getBoard().getBoardRegion())
                    .boardTag(liked.getBoard().getBoardTag())
                    .FirstImg(liked.getBoard().getImgFiles().getFirstImgUrl())
                    .isliked(true)
                    .modifiedAt(liked.getBoard().getModifiedAt())
                    .userNickName(liked.getBoard().getUser().getNickName())
                    .category(liked.getBoard().getCategory())
                    .build();

            likedResponseDtoList.add(likedResponseDto);
        }
        return likedResponseDtoList;
    }

    //5번 API 등록한 목록 (구현 완료)
    @Transactional
    public List<BoardResponseDto.MyBoard> getMyBoard(Long userId) {
        // userid를 사용하여 board에서 있는 것들 다 찾아오고 그에 따른 내용들을 전달
        List<BoardResponseDto.MyBoard> myBoardResponseDtoList = new ArrayList<>();

        // 없으면 for문 안돌고 빈 list가 들어간다.
        List<Board> boardList = boardRepository.findListBoardByUserId(userId);



        for (Board board : boardList) {
            //Boolean islLked = boardValidator.DefaultMyBoardLiked(userId,board.getId());

            BoardResponseDto.MyBoard myBoardResponseDto = BoardResponseDto.MyBoard.builder()
                    .boardId(board.getId())
                    .boardTitle(board.getTitle())
                    .boardTag(board.getBoardTag())
                    .FirstImg(board.getImgFiles().getFirstImgUrl())
                    .modifiedAt(board.getModifiedAt())
                    .dailyRentalFee(board.getAmount().getDailyRentalFee())
                    .nickName(board.getUser().getNickName())
                    .category(board.getCategory())
                    .build();

            myBoardResponseDtoList.add(myBoardResponseDto);


        }
        return myBoardResponseDtoList;
    }

    //5번 API 내가 대여한 목록 불러오기 (구현 완료)
    @Transactional
    public List<UserResponseDto.RentBuyer> getBuyList(Long userid) {
        List<UserResponseDto.RentBuyer> rentBuyerResponseDtoList = new ArrayList<>();

        // 없으면 for문 안돌고 빈 list가 들어간다.
        List<Auth> authList = authRepository.findByBuyerId(userid);

        for (Auth auth : authList) {
            UserResponseDto.RentBuyer rentBuyerResponseDto = UserResponseDto.RentBuyer.builder()
                    .boardId(auth.getBoard().getId())
                    .boardTitle(auth.getBoard().getTitle())
                    .boardTag(auth.getBoard().getBoardTag())
                    .boardRegion(auth.getBoard().getBoardRegion())
                    .FirstImgUrl(auth.getBoard().getImgFiles().getFirstImgUrl())
                    .dailyRentalFee(auth.getBoard().getAmount().getDailyRentalFee())
                    .startRental(auth.getStartRental())
                    .nickName(auth.getAuthSeller().getNickName())
                    .authId(auth.getId())
                    .category(auth.getBoard().getCategory())
                    .build();
            rentBuyerResponseDtoList.add(rentBuyerResponseDto);
        }
        return rentBuyerResponseDtoList;

    }


    //5번 API 내가 빌려준 목록 불러오기 (구현 완료)
    @Transactional
    public List<UserResponseDto.RentSeller> getSellList(Long userid) {
        List<UserResponseDto.RentSeller> rentSellerResponseDtoList = new ArrayList<>();

        // 없으면 for문 안돌고 빈 list가 들어간다.
        List<Auth> authList = authRepository.findBySellerId(userid);

        for (int i = 0; i < authList.size(); i++) {
            UserResponseDto.RentSeller rentSellerResponseDto = UserResponseDto.RentSeller.builder()
                    .boardId(authList.get(i).getBoard().getId())
                    .boardTitle(authList.get(i).getBoard().getTitle())
                    .boardRegion(authList.get(i).getBoard().getBoardRegion())
                    .boardTag(authList.get(i).getBoard().getBoardTag())
                    .FirstImgUrl(authList.get(i).getBoard().getImgFiles().getFirstImgUrl())
                    .dailyRentalFee(authList.get(i).getBoard().getAmount().getDailyRentalFee())
                    .startRental(authList.get(i).getStartRental())
                    .endRental(authList.get(i).getEndRental())
                    .nickName(authList.get(i).getAuthBuyer().getNickName())
                    .authId(authList.get(i).getId())
                    .category(authList.get(i).getBoard().getCategory())
                    .build();
            rentSellerResponseDtoList.add(rentSellerResponseDto);
        }
        return rentSellerResponseDtoList;
    }


    //6번 API 회원 정보 수정 (구현 완료)
    @Transactional
    public BasicResponseDTO usermodifyService(Long userid, UserRequestDto.Modify modifyRequestDTO) {
        User user = userValidator.ValidByUserId(userid);

        //유저 이미지가 변경 되었을 때03
        if (modifyRequestDTO.getUserImg() != null) {
            user.updateUserImg(modifyRequestDTO);
        }
        // 아닐때
        else {
            user.updateEtc(modifyRequestDTO);
        }
        return BasicResponseDTO.builder()
                .result("success")
                .msg(user.getNickName() + "님 게시글 수정 성공하였습니다.")
                .build();

    }


    //7번 API 회원 탈퇴 (구현 완료)
    @Transactional
    public String UserDelete(Long userid, UserRequestDto.Login userLoginRequest) {
        //userid에 의한 user가 있는지 판단
        User user = userValidator.ValidByUserDelete(userid, userLoginRequest);

        //파일 이미지 key를 반환하기 위한 로직
        String fileName = user.getUserImg().substring(user.getUserImg().lastIndexOf("/")+1);


        // 프로필 이미지 삭제 후, 회원탈퇴
        awsS3Service.deleteProfileImg(fileName);

        userRepository.deleteById(userid);
        return user.getNickName();
    }


}
