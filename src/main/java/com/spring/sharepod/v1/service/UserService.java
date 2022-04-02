package com.spring.sharepod.v1.service;

import com.spring.sharepod.entity.User;
import com.spring.sharepod.exception.CommonError.ErrorCode;
import com.spring.sharepod.exception.CommonError.ErrorCodeException;
import com.spring.sharepod.jwt.JwtTokenProvider;
import com.spring.sharepod.v1.dto.request.UserRequestDto;
import com.spring.sharepod.v1.dto.response.BasicResponseDTO;
import com.spring.sharepod.v1.dto.response.Board.MyBoardResponseDto;
import com.spring.sharepod.v1.dto.response.Liked.LikedListResponseDto;
import com.spring.sharepod.v1.dto.response.RentBuyer;
import com.spring.sharepod.v1.dto.response.RentSeller;
import com.spring.sharepod.v1.dto.response.User.UserInfoResponseDto;
import com.spring.sharepod.v1.dto.response.User.UserMyInfoResponseDto;
import com.spring.sharepod.v1.dto.response.User.UserReservation;
import com.spring.sharepod.v1.dto.response.User.UserResponseDto;
import com.spring.sharepod.v1.repository.Board.BoardRepository;
import com.spring.sharepod.v1.repository.UserRepository;
import com.spring.sharepod.v1.validator.BoardValidator;
import com.spring.sharepod.v1.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.spring.sharepod.exception.CommonError.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;
    private final AwsS3Service awsS3Service;
    private final BoardValidator boardValidator;
    private final EntityManager entityManager;

    //1번 API 로그인
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
    public BasicResponseDTO reissue(UserRequestDto.Reissue reissue, HttpServletResponse res, HttpServletRequest req) {

        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken(), req)) {
            //fail로 리턴이 나올 경우 refresttoken 정보가 유효하지 않다고 보내면서 프론트가 다시 로그인 시킨다.
            throw new ErrorCodeException(RETOKEN_REISSUE);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = redisTemplate.opsForValue().get("RT:" + authentication.getName());


        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if (ObjectUtils.isEmpty(refreshToken)) {
            return BasicResponseDTO.builder()
                    .result("fail")
                    .msg("잘못된 요청입니다.")
                    .build();
        }

        if (!refreshToken.equals(reissue.getRefreshToken())) {
            throw new ErrorCodeException(RETOKEN_REISSUE);
        }

        // 4. 새로운 토큰 생성
        UserResponseDto.LoginReFreshToken tokenInfo = jwtTokenProvider.generateToken(authentication, user.getId());



        HttpHeaders header = new HttpHeaders();
        header.set("accessToken", tokenInfo.getAccessToken());
        header.set("refreshToken", tokenInfo.getRefreshToken());

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS);

        return BasicResponseDTO.builder()
                .result("success")
                .msg("accessToken 정보가 갱신되었습니다.")
                .build();
    }

    //3번 API 로그아웃
    public BasicResponseDTO logout(UserRequestDto.Reissue reIssueRequestDto, HttpServletRequest req) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(reIssueRequestDto.getAccessToken(), req)) {
            throw new ErrorCodeException(ACCTOKEN_REISSUE);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reIssueRequestDto.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        } else {
            throw new ErrorCodeException(RETOKEN_REISSUE);
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(reIssueRequestDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(reIssueRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return BasicResponseDTO.builder()
                .result("success")
                .msg("로그아웃 성공")
                .build();
    }


    //4번 API 회원가입
    @Transactional
    public BasicResponseDTO registerUser(UserRequestDto.Register userRegisterRequestDto) {
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
        return BasicResponseDTO.builder()
                .result("success")
                .msg(userRegisterRequestDto.getNickName()+" 님 회원가입 성공하였습니다.")
                .build();
    }

    //5번 API userinfo 불러오기
    @Transactional
    public UserMyInfoResponseDto getUserInfo(Long userid) {
        TypedQuery<UserInfoResponseDto> query = entityManager.createQuery("SELECT NEW com.spring.sharepod.v1.dto.response.User.UserInfoResponseDto(u.id,u.username,u.nickName,u.userRegion,u.userImg,u.createdAt)  FROM User u where u.id=:userId", UserInfoResponseDto.class);
        query.setParameter("userId",userid);
        UserInfoResponseDto resultList = query.getSingleResult();

        if(resultList==null){
            throw new ErrorCodeException(USER_NOT_FOUND);
        }

        return UserMyInfoResponseDto.builder()
                .result("success")
                .msg("마이 페이지 불러오기 성공")
                .userInfo(resultList)
                .build();
    }

    //5번 API 찜목록 불러오기
    @Transactional
    public UserResponseDto.UserLikedList getUserLikeBoard(Long userid) {
        TypedQuery<LikedListResponseDto> query = entityManager.createQuery("SELECT NEW com.spring.sharepod.v1.dto.response.Liked.LikedListResponseDto(b.id,b.title,b.boardRegion,b.boardTag,b.imgFiles.firstImgUrl,true,b.modifiedAt,b.amount.dailyRentalFee,b.user.nickName,b.category)  FROM Liked l inner JOIN Board b on l.board.id = b.id where l.user.id=:userId", LikedListResponseDto.class);
        query.setParameter("userId",userid);
        List<LikedListResponseDto> resultList = query.getResultList();

        return UserResponseDto.UserLikedList.builder()
                .result("success")
                .msg("찜 목록 GET 성공")
                .userLikedBoard(resultList)
                .build();
    }

    //5번 API 등록한 목록
    @Transactional
    public UserResponseDto.UserMyBoardList getMyBoard(Long userId) {

        Boolean isLiked = false;
        List<MyBoardResponseDto> querydslMyBoardList = boardRepository.getMyBoard(userId);

        int resultCount = querydslMyBoardList.size();

        for (int i=0;i<resultCount;i++){
            isLiked = boardValidator.DefaultLiked(Optional.ofNullable(userId),querydslMyBoardList.get(i).getId());
            querydslMyBoardList.get(i).setIsLiked(Optional.ofNullable(isLiked));
        }

        return UserResponseDto.UserMyBoardList.builder()
                .result("success")
                .msg("등록한 게시글 GET 성공")
                .userMyBoard(querydslMyBoardList)
                .build();
    }

    //5번 API 내가 대여한 목록 불러오기
    @Transactional
    public List<RentBuyer> getBuyList(Long userId) {
        Boolean isLiked = false;
        List<RentBuyer> querydslRentBuyerList = boardRepository.getRentBuyer(userId);
        int resultCount = querydslRentBuyerList.size();
        for (int i=0;i<resultCount;i++){
            isLiked = boardValidator.DefaultLiked(Optional.ofNullable(userId),querydslRentBuyerList.get(i).getId());
            querydslRentBuyerList.get(i).setIsLiked(Optional.ofNullable(isLiked));



        }

//        List<UserResponseDto.RentBuyer> rentBuyerResponseDtoList = new ArrayList<>();
//        // 없으면 for문 안돌고 빈 list가 들어간다.
//        List<Auth> authList = authRepository.findByBuyerId(userId);
//
//        for (Auth auth : authList) {
//            Boolean isLiked = boardValidator.DefaultLiked(Optional.ofNullable(userId),auth.getBoard().getId());
//
//            UserResponseDto.RentBuyer rentBuyerResponseDto = UserResponseDto.RentBuyer.builder()
//                    .boardId(auth.getBoard().getId())
//                    .boardTitle(auth.getBoard().getTitle())
//                    .boardTag(auth.getBoard().getBoardTag())
//                    .boardRegion(auth.getBoard().getBoardRegion())
//                    .isLiked(isLiked)
//                    .FirstImgUrl(auth.getBoard().getImgFiles().getFirstImgUrl())
//                    .dailyRentalFee(auth.getBoard().getAmount().getDailyRentalFee())
//                    .startRental(auth.getStartRental())
//                    .nickName(auth.getAuthSeller().getNickName())
//                    .authId(auth.getId())
//                    .category(auth.getBoard().getCategory())
//                    .build();
//            rentBuyerResponseDtoList.add(rentBuyerResponseDto);
//        }
        return querydslRentBuyerList;
    }


    //5번 API 내가 빌려준 목록 불러오기
    @Transactional
    public List<RentSeller> getSellList(Long userId) {
        Boolean isLiked = false;
         List<RentSeller> querydslRentSellerList = boardRepository.getRentSeller(userId);
        int resultCount = querydslRentSellerList.size();
        for (int i=0;i<resultCount;i++){
            isLiked = boardValidator.DefaultLiked(Optional.ofNullable(userId),querydslRentSellerList.get(i).getId());
            querydslRentSellerList.get(i).setIsLiked(Optional.ofNullable(isLiked));
        }
        return querydslRentSellerList;
    }
    @Transactional
    public List<UserReservation> getReservationList(Long userId) {
        Boolean isLiked = false;


        List<UserReservation> querydslResrvationList = boardRepository.getReservation(userId);
        int resultCount = querydslResrvationList.size();

        for (int i=0;i<resultCount;i++){
            isLiked = boardValidator.DefaultLiked(Optional.ofNullable(userId),querydslResrvationList.get(i).getId());
            querydslResrvationList.get(i).setIsLiked(Optional.ofNullable(isLiked));
        }
        return querydslResrvationList;
    }


    //6번 API 회원 정보 수정
    @Transactional
    public UserResponseDto.UserModifiedInfo usermodifyService(Long userid, UserRequestDto.Modify modifyRequestDTO) {
        User user = userValidator.ValidByUserId(userid);

        //유저 이미지가 변경 되었을 때
        if (!Objects.equals(modifyRequestDTO.getUserImg(), user.getUserImg())) {
            user.updateUserImg(modifyRequestDTO);
        }
        // 아닐때
        else {
            user.updateEtc(modifyRequestDTO);
        }

        return UserResponseDto.UserModifiedInfo.builder()
                .result("success")
                .msg(user.getNickName() + "님 회원정보 수정 성공하였습니다.")
                .userId(userid)
                .username(user.getUsername())
                .userNickname(user.getNickName())
                .userRegion(user.getUserRegion())
                .userModifiedImg(user.getUserImg())
                .build();
    }

    //7번 API 회원 탈퇴
    @Transactional
    public BasicResponseDTO UserDelete(Long userid, UserRequestDto.Login userLoginRequest) {
        //userid에 의한 user가 있는지 판단
        User user = userValidator.ValidByUserDelete(userid, userLoginRequest);

        //파일 이미지 key를 반환하기 위한 로직
        String fileName = user.getUserImg().substring(user.getUserImg().lastIndexOf("/")+1);

        // 프로필 이미지 삭제 후, 회원탈퇴
        awsS3Service.deleteProfileImg(fileName);

        userRepository.deleteById(userid);
        return BasicResponseDTO.builder()
                .result("success")
                .msg("회원탈퇴 성공")
                .build();
    }
}
