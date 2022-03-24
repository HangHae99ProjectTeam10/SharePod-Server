package com.spring.sharepod.jwt;


import com.spring.sharepod.exception.TokenError.TokenErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    public static final String AUTHORIZATION_HEADER = "accessToken";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        // 헤더에서 JWT 를 받아옵니다.
//        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
//        System.out.println("token-------------"+token);
//
//        // 유효한 토큰인지 확인합니다.
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//
//            // (추가) Redis 에 해당 accessToken logout 여부 확인
//            String isLogout = (String)redisTemplate.opsForValue().get(token);
//            if (ObjectUtils.isEmpty(isLogout)) {
//                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
//                Authentication authentication = jwtTokenProvider.getAuthentication(token);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//
//        chain.doFilter(request, response);
//    }



//        String token = getToken(request);
//        System.out.println(token);
        String token = jwtTokenProvider.resolveToken(request);
        //String token = getToken(request);
        System.out.println(token);
        try {
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token,request)) {
                System.out.println(token + "if문 안에 들어옴");
//                String isLogout = (String) redisTemplate.opsForValue().get(token);
//                if (ObjectUtils.isEmpty(isLogout)) {
//                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
//                    System.out.println(authentication);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                System.out.println(authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", TokenErrorCode.WRONG_TYPE_TOKEN.getErrorMessage());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", TokenErrorCode.EXPIRED_TOKEN.getErrorMessage());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", TokenErrorCode.UNSUPPORTED_TOKEN.getErrorMessage());
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", TokenErrorCode.WRONG_TOKEN.getErrorMessage());
        }
        catch (Exception e) {
            log.error("================================================");
            log.error("JwtFilter - doFilterInternal() 오류발생");
            log.error("token : {}", token);
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            e.printStackTrace();
            log.error("}");
            log.error("================================================");
            request.setAttribute("exception", TokenErrorCode.UNKNOWN_ERROR.getErrorMessage());
        }

        chain.doFilter(request, response);


    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }


}