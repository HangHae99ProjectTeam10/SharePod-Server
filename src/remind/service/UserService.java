package com.spring.sharepod.service;

import com.spring.sharepod.dto.request.User.ReIssueRequestDto;
import com.spring.sharepod.dto.request.User.UserLoginRequest;
import com.spring.sharepod.dto.request.User.UserModifyRequestDTO;
import com.spring.sharepod.dto.request.User.UserRegisterRequestDto;
import com.spring.sharepod.dto.response.BasicResponseDTO;
import com.spring.sharepod.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.dto.response.Board.RentBuyerResponseDto;
import com.spring.sharepod.dto.response.Board.RentSellerResponseDto;
import com.spring.sharepod.dto.response.Liked.LikedResponseDto;
import com.spring.sharepod.dto.response.User.LoginReFreshTokenResponseDto;
import com.spring.sharepod.dto.response.User.LoginReturnResponseDTO;
import com.spring.sharepod.dto.response.User.UserInfoResponseDto;
import com.spring.sharepod.entity.Auth;
import com.spring.sharepod.entity.Board;
import com.spring.sharepod.entity.Liked;
import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.model.LogOut;
import com.spring.sharepod.model.ReFreshToken;
import com.spring.sharepod.repository.AuthRepository;
import com.spring.sharepod.repository.BoardRepository;
import com.spring.sharepod.repository.LikedRepository;
import com.spring.sharepod.repository.UserRepository;
import com.spring.sharepod.security.jwt.JwtTokenProvider;
import com.spring.sharepod.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    //로그인
    @Transactional
    public LoginReturnResponseDTO loginReturnDTO(UserLoginRequest userLoginRequest, HttpServletResponse res) {
        User user = userRepository.findByUsername(userLoginRequest.getUsername()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

        //비밀번호 다르면
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
        LoginReFreshTokenResponseDto loginReFreshTokenResponseDto = jwtTokenProvider.generateToken(authentication, user.getId());

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), loginReFreshTokenResponseDto.getRefreshToken(), loginReFreshTokenResponseDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);


//        String tokenDto = jwtTokenProvider.createToken(user.getUsername(), user.getRoles(),user.getId());
        res.addHeader("accessToken", loginReFreshTokenResponseDto.getAccessToken());
        res.addHeader("refreshToken", loginReFreshTokenResponseDto.getRefreshToken());

        return LoginReturnResponseDTO.builder()
                .result("success")
                .msg("로그인 성공")
                .userid(user.getId())
                .nickname(user.getNickname())
                .mapdata(user.getMapdata())
                .userimg(user.getUserimg())
                .build();
    }

    // 리프레쉬 토큰 재발급
    public ResponseEntity<ReFreshToken> reissue(ReIssueRequestDto reissue,HttpServletResponse res, HttpServletRequest req) {
        System.out.println("reissue controller 1");

        System.out.println(reissue.getRefreshToken()+ "refreshtoken 출력(request에서 받아온 내용)");
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken(),req)) {
            //fail로 리턴이 나올 경우 refresttoken 정보가 유효하지 않다고 보내면서 프론트가 다시 로그인 시킨다.
            return new ResponseEntity<>(new ReFreshToken("fail","Refresh Token 정보가 유효하지 않습니다."),HttpStatus.BAD_REQUEST);
        }

        System.out.println("reissue controller 2");

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        System.out.println(authentication.getName() + "authentication.getName()");

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));


        System.out.println("RT 1");
        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = redisTemplate.opsForValue().get("RT:" + authentication.getName());
        System.out.println("RT 2");


        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return new ResponseEntity<>(new ReFreshToken("fail","잘못된 요청입니다."),HttpStatus.BAD_REQUEST);
        }

        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return new ResponseEntity<>(new ReFreshToken("fail","ReFreshToken의 정보가 일치하지 않습니다."),HttpStatus.BAD_REQUEST);
        }

        System.out.println("RT 3");

        System.out.println("userid 들어오는지 확인" );

        System.out.println("RT 4");


        // 4. 새로운 토큰 생성
        LoginReFreshTokenResponseDto tokenInfo = jwtTokenProvider.generateToken(authentication, user.getId());

        res.addHeader("accessToken", tokenInfo.getAccessToken());
        res.addHeader("refreshToken", tokenInfo.getRefreshToken());


        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return new ResponseEntity<>(new ReFreshToken("success","accessToken 정보가 갱신되었습니다."),HttpStatus.OK);
    }

    //로그아웃
    public ResponseEntity<LogOut> logout(ReIssueRequestDto reIssueRequestDto, HttpServletRequest req) {
        System.out.println(reIssueRequestDto.getAccessToken() + "============== reIssueRequestDto.getAccessToken()");

        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(reIssueRequestDto.getAccessToken(),req)) {
            return new ResponseEntity<>(new LogOut("fail","엑세스 토큰이 만료되었습니다.Reissue를 통해서 토큰을 재 발급 후 로그아웃으로 다시 와주세요."),HttpStatus.BAD_REQUEST);
        }

        System.out.println("logout 1");

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reIssueRequestDto.getAccessToken());

        System.out.println("logout2");

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }else{
            return new ResponseEntity<>(new LogOut("fail","리프레쉬이 만료되었습니다. 로그인을 다시 이용해 주세요."),HttpStatus.BAD_REQUEST);
        }

        System.out.println("logout3");

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(reIssueRequestDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(reIssueRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        System.out.println("logout 4");

        return new ResponseEntity<>(new LogOut("success","로그아웃이 되었습니다!"),HttpStatus.OK);
    }




    // 회원가입
    @Transactional
    public Long createUser(UserRegisterRequestDto userRegisterRequestDto) {
        //회원가입 유효성 검사 validator 통해서 검증한다. 중간에 이상하거 있으면 바로 거기서 메시지 반환하도록


        // 여기서부터는 검증된 데이터들이기에 그냥 비밀번호 암호화하고 빌더 패턴으로 유저에 대한 정보를 생성한다.
        // 비밀번호 암호화
        //String password = passwordEncoder.encode(userRegisterRequestDto.getPassword());

        // 유저 생성
        User user = User.builder()
                .userimg(userRegisterRequestDto.getUserimg())
                .username(userRegisterRequestDto.getUsername())
                .password(passwordEncoder.encode(userRegisterRequestDto.getPassword()))
                .mapdata(userRegisterRequestDto.getMapdata())
                .nickname(userRegisterRequestDto.getNickname())
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build();

        // 유저 저장하기
        userRepository.save(user);
        return user.getId();
    }

    //userinfo 불러오기
    @Transactional
    public UserInfoResponseDto getUserInfo(Long userid) {
        User user = userValidator.ValidByUserId(userid);

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
    public List<LikedResponseDto> getUserLikeBoard(Long userid) {
        //해당하는 유저가 존재하는 like 테이블에서 boardid를 받아오고 그 boardid를 통해
        // boardtitle과 userid category를 찾아낸다.
        List<LikedResponseDto> likedResponseDtoList = new ArrayList<>();

        // 없으면 for문 안돌고 빈 list가 들어간다.
        List<Liked> userlikeList = likedRepository.findByUserId(userid);

        for (Liked liked : userlikeList) {
            //Board likedBoardList = likedRepository.findByBoardId(userliked.getBoard().getId());

            LikedResponseDto likedResponseDto = LikedResponseDto.builder()
                    .boardid(liked.getBoard().getId())
                    .boardtitle(liked.getBoard().getTitle())
                    .userid(liked.getUser().getId())
                    .category(liked.getBoard().getCategory())
                    .build();

            likedResponseDtoList.add(likedResponseDto);
        }
        return likedResponseDtoList;
    }

    //내가 등록한 글 불러오기
    @Transactional
    public List<MyBoardResponseDto> getMyBoard(Long userid) {
        // userid를 사용하여 board에서 있는 것들 다 찾아오고 그에 따른 내용들을 전달
        List<MyBoardResponseDto> myBoardResponseDtoList = new ArrayList<>();

        // 없으면 for문 안돌고 빈 list가 들어간다.
        List<Board> boardList = boardRepository.findByUserId(userid);

        for (Board board : boardList) {
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

        // 없으면 for문 안돌고 빈 list가 들어간다.
        List<Auth> authList = authRepository.findByBuyerId(userid);

        for (Auth auth : authList) {
            RentBuyerResponseDto rentBuyerResponseDto = RentBuyerResponseDto.builder()
                    .boardid(auth.getBoard().getId())
                    .boardtitle(auth.getBoard().getTitle())
                    .nickname(auth.getAuthseller().getNickname())
                    .authid(auth.getId())
                    .category(auth.getBoard().getCategory())
                    .build();
            rentBuyerResponseDtoList.add(rentBuyerResponseDto);
        }
        return rentBuyerResponseDtoList;

    }


    //내가 빌려준 목록 불러오기
    @Transactional
    public List<RentSellerResponseDto> getSellList(Long userid) {
        List<RentSellerResponseDto> rentSellerResponseDtoList = new ArrayList<>();

        // 없으면 for문 안돌고 빈 list가 들어간다.
        List<Auth> authList = authRepository.findBySellerId(userid);

        for (Auth auth : authList) {
            RentSellerResponseDto rentSellerResponseDto = RentSellerResponseDto.builder()
                    .boardid(auth.getBoard().getId())
                    .boardtitle(auth.getBoard().getTitle())
                    .nickname(auth.getAuthbuyer().getNickname())
                    .authid(auth.getId())
                    .category(auth.getBoard().getCategory())
                    .build();
            rentSellerResponseDtoList.add(rentSellerResponseDto);
        }
        return rentSellerResponseDtoList;
    }

    //회원 탈퇴
    @Transactional
    public String UserDelete(Long userid, UserLoginRequest userLoginRequest) {
        //userid에 의한 user가 있는지 판단
        User user = userValidator.ValidByUserDelete(userid, userLoginRequest);

        String fileName = user.getUserimg().substring(user.getUserimg().lastIndexOf("/")+1);;

        System.out.println("filename " + user.getUserimg());

        // 프로필 이미지 삭제 후, 회원탈퇴
        awsS3Service.deleteProfileImg(fileName);

        userRepository.deleteById(userid);
        return user.getNickname();
    }

    //회원 정보 수정
    @Transactional
    public BasicResponseDTO usermodifyService(Long userid, UserModifyRequestDTO modifyRequestDTO) {
        User user = userValidator.ValidByUserId(userid);

        //유저 이미지가 변경 되었을 때
        if (modifyRequestDTO.getUserimg() != null) {
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
