package com.spring.sharepod.jwt;

import com.spring.sharepod.exception.TokenError.TokenErrorCode;
import com.spring.sharepod.v1.dto.response.UserResponseDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "roles";
    private static final String BEARER_TYPE = "Bearer";

    private String secretKey = "webfirewood";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;              // //1분
    //private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 2 * 7 * 24 * 60 * 60 * 1000L;     // 10초

    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public UserResponseDto.LoginReFreshToken generateToken(Authentication authentication, Long userid) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        System.out.println("예상 == user ++++++++++++ " + authorities);

        Claims claims = Jwts.claims().setSubject(authentication.getName());
        System.out.println("authentication.getName() ==    " + authentication.getName());

        claims.put("roles", authorities);
        claims.put("userid", userid);


        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claims)
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return UserResponseDto.LoginReFreshToken.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());

        System.out.println(principal);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    // Request의 Header에서 token 값을 가져옵니다. "accessToken" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }


    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken, HttpServletRequest request) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", TokenErrorCode.WRONG_TYPE_TOKEN.getErrorMessage());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", TokenErrorCode.EXPIRED_TOKEN.getErrorMessage());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", TokenErrorCode.UNSUPPORTED_TOKEN.getErrorMessage());
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", TokenErrorCode.WRONG_TOKEN.getErrorMessage());
        }
        return false;
    }


    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}