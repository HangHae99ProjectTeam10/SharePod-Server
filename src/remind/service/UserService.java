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

    //?????????
    @Transactional
    public LoginReturnResponseDTO loginReturnDTO(UserLoginRequest userLoginRequest, HttpServletResponse res) {
        User user = userRepository.findByUsername(userLoginRequest.getUsername()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));

        //???????????? ?????????
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new ErrorCodeException(ErrorCode.PASSWORD_COINCIDE);
        }

        // 1. Login ID/PW ??? ???????????? Authentication ?????? ??????
        // ?????? authentication ??? ?????? ????????? ???????????? authenticated ?????? false
        UsernamePasswordAuthenticationToken authenticationToken = userLoginRequest.toAuthentication();

        // 2. ?????? ?????? (????????? ???????????? ??????)??? ??????????????? ??????
        // authenticate ???????????? ????????? ??? CustomUserDetailsService ?????? ?????? loadUserByUsername ???????????? ??????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. ?????? ????????? ???????????? JWT ?????? ??????
        LoginReFreshTokenResponseDto loginReFreshTokenResponseDto = jwtTokenProvider.generateToken(authentication, user.getId());

        // 4. RefreshToken Redis ?????? (expirationTime ????????? ?????? ?????? ?????? ??????)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), loginReFreshTokenResponseDto.getRefreshToken(), loginReFreshTokenResponseDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);


//        String tokenDto = jwtTokenProvider.createToken(user.getUsername(), user.getRoles(),user.getId());
        res.addHeader("accessToken", loginReFreshTokenResponseDto.getAccessToken());
        res.addHeader("refreshToken", loginReFreshTokenResponseDto.getRefreshToken());

        return LoginReturnResponseDTO.builder()
                .result("success")
                .msg("????????? ??????")
                .userid(user.getId())
                .nickname(user.getNickname())
                .mapdata(user.getMapdata())
                .userimg(user.getUserimg())
                .build();
    }

    // ???????????? ?????? ?????????
    public ResponseEntity<ReFreshToken> reissue(ReIssueRequestDto reissue,HttpServletResponse res, HttpServletRequest req) {
        System.out.println("reissue controller 1");

        System.out.println(reissue.getRefreshToken()+ "refreshtoken ??????(request?????? ????????? ??????)");
        // 1. Refresh Token ??????
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken(),req)) {
            //fail??? ????????? ?????? ?????? refresttoken ????????? ???????????? ????????? ???????????? ???????????? ?????? ????????? ?????????.
            return new ResponseEntity<>(new ReFreshToken("fail","Refresh Token ????????? ???????????? ????????????."),HttpStatus.BAD_REQUEST);
        }

        System.out.println("reissue controller 2");

        // 2. Access Token ?????? User email ??? ???????????????.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        System.out.println(authentication.getName() + "authentication.getName()");

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new ErrorCodeException(USER_NOT_FOUND));


        System.out.println("RT 1");
        // 3. Redis ?????? User email ??? ???????????? ????????? Refresh Token ?????? ???????????????.
        String refreshToken = redisTemplate.opsForValue().get("RT:" + authentication.getName());
        System.out.println("RT 2");


        // (??????) ?????????????????? Redis ??? RefreshToken ??? ???????????? ?????? ?????? ??????
        if(ObjectUtils.isEmpty(refreshToken)) {
            return new ResponseEntity<>(new ReFreshToken("fail","????????? ???????????????."),HttpStatus.BAD_REQUEST);
        }

        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return new ResponseEntity<>(new ReFreshToken("fail","ReFreshToken??? ????????? ???????????? ????????????."),HttpStatus.BAD_REQUEST);
        }

        System.out.println("RT 3");

        System.out.println("userid ??????????????? ??????" );

        System.out.println("RT 4");


        // 4. ????????? ?????? ??????
        LoginReFreshTokenResponseDto tokenInfo = jwtTokenProvider.generateToken(authentication, user.getId());

        res.addHeader("accessToken", tokenInfo.getAccessToken());
        res.addHeader("refreshToken", tokenInfo.getRefreshToken());


        // 5. RefreshToken Redis ????????????
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return new ResponseEntity<>(new ReFreshToken("success","accessToken ????????? ?????????????????????."),HttpStatus.OK);
    }

    //????????????
    public ResponseEntity<LogOut> logout(ReIssueRequestDto reIssueRequestDto, HttpServletRequest req) {
        System.out.println(reIssueRequestDto.getAccessToken() + "============== reIssueRequestDto.getAccessToken()");

        // 1. Access Token ??????
        if (!jwtTokenProvider.validateToken(reIssueRequestDto.getAccessToken(),req)) {
            return new ResponseEntity<>(new LogOut("fail","????????? ????????? ?????????????????????.Reissue??? ????????? ????????? ??? ?????? ??? ?????????????????? ?????? ????????????."),HttpStatus.BAD_REQUEST);
        }

        System.out.println("logout 1");

        // 2. Access Token ?????? User email ??? ???????????????.
        Authentication authentication = jwtTokenProvider.getAuthentication(reIssueRequestDto.getAccessToken());

        System.out.println("logout2");

        // 3. Redis ?????? ?????? User email ??? ????????? Refresh Token ??? ????????? ????????? ?????? ??? ?????? ?????? ???????????????.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token ??????
            redisTemplate.delete("RT:" + authentication.getName());
        }else{
            return new ResponseEntity<>(new LogOut("fail","??????????????? ?????????????????????. ???????????? ?????? ????????? ?????????."),HttpStatus.BAD_REQUEST);
        }

        System.out.println("logout3");

        // 4. ?????? Access Token ???????????? ????????? ?????? BlackList ??? ????????????
        Long expiration = jwtTokenProvider.getExpiration(reIssueRequestDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(reIssueRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        System.out.println("logout 4");

        return new ResponseEntity<>(new LogOut("success","??????????????? ???????????????!"),HttpStatus.OK);
    }




    // ????????????
    @Transactional
    public Long createUser(UserRegisterRequestDto userRegisterRequestDto) {
        //???????????? ????????? ?????? validator ????????? ????????????. ????????? ???????????? ????????? ?????? ????????? ????????? ???????????????


        // ?????????????????? ????????? ????????????????????? ?????? ???????????? ??????????????? ?????? ???????????? ????????? ?????? ????????? ????????????.
        // ???????????? ?????????
        //String password = passwordEncoder.encode(userRegisterRequestDto.getPassword());

        // ?????? ??????
        User user = User.builder()
                .userimg(userRegisterRequestDto.getUserimg())
                .username(userRegisterRequestDto.getUsername())
                .password(passwordEncoder.encode(userRegisterRequestDto.getPassword()))
                .mapdata(userRegisterRequestDto.getMapdata())
                .nickname(userRegisterRequestDto.getNickname())
                .roles(Collections.singletonList("ROLE_USER")) // ?????? ????????? USER ??? ??????
                .build();

        // ?????? ????????????
        userRepository.save(user);
        return user.getId();
    }

    //userinfo ????????????
    @Transactional
    public UserInfoResponseDto getUserInfo(Long userid) {
        User user = userValidator.ValidByUserId(userid);

        //build?????? ?????? user??? ?????? ??? ????????? responseDto??? ????????? ????????????.
        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
                .userid(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .mapdata(user.getMapdata())
                .userimg(user.getUserimg())
                .build();

        return userInfoResponseDto;
    }

    //????????? ????????????
    @Transactional
    public List<LikedResponseDto> getUserLikeBoard(Long userid) {
        //???????????? ????????? ???????????? like ??????????????? boardid??? ???????????? ??? boardid??? ??????
        // boardtitle??? userid category??? ????????????.
        List<LikedResponseDto> likedResponseDtoList = new ArrayList<>();

        // ????????? for??? ????????? ??? list??? ????????????.
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

    //?????? ????????? ??? ????????????
    @Transactional
    public List<MyBoardResponseDto> getMyBoard(Long userid) {
        // userid??? ???????????? board?????? ?????? ?????? ??? ???????????? ?????? ?????? ???????????? ??????
        List<MyBoardResponseDto> myBoardResponseDtoList = new ArrayList<>();

        // ????????? for??? ????????? ??? list??? ????????????.
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

    //?????? ????????? ?????? ????????????
    @Transactional
    public List<RentBuyerResponseDto> getBuyList(Long userid) {
        List<RentBuyerResponseDto> rentBuyerResponseDtoList = new ArrayList<>();

        // ????????? for??? ????????? ??? list??? ????????????.
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


    //?????? ????????? ?????? ????????????
    @Transactional
    public List<RentSellerResponseDto> getSellList(Long userid) {
        List<RentSellerResponseDto> rentSellerResponseDtoList = new ArrayList<>();

        // ????????? for??? ????????? ??? list??? ????????????.
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

    //?????? ??????
    @Transactional
    public String UserDelete(Long userid, UserLoginRequest userLoginRequest) {
        //userid??? ?????? user??? ????????? ??????
        User user = userValidator.ValidByUserDelete(userid, userLoginRequest);

        String fileName = user.getUserimg().substring(user.getUserimg().lastIndexOf("/")+1);;

        System.out.println("filename " + user.getUserimg());

        // ????????? ????????? ?????? ???, ????????????
        awsS3Service.deleteProfileImg(fileName);

        userRepository.deleteById(userid);
        return user.getNickname();
    }

    //?????? ?????? ??????
    @Transactional
    public BasicResponseDTO usermodifyService(Long userid, UserModifyRequestDTO modifyRequestDTO) {
        User user = userValidator.ValidByUserId(userid);

        //?????? ???????????? ?????? ????????? ???
        if (modifyRequestDTO.getUserimg() != null) {
            user.update1(modifyRequestDTO);
        }
        // ?????????
        else {
            user.update2(modifyRequestDTO);
        }

        return BasicResponseDTO.builder()
                .result("success")
                .msg("?????? ??????")
                .build();

    }
}
