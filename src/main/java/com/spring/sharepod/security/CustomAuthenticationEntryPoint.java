package com.spring.sharepod.security;

import com.spring.sharepod.exception.TokenError.TokenErrorCode;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String) request.getAttribute("exception");

        System.out.println("exception          " + exception);

        if (exception == null) {
            setResponse(response, TokenErrorCode.UNKNOWN_ERROR);

        }
        //잘못된 타입의 토큰인 경우
        else if (exception.equals(TokenErrorCode.WRONG_TYPE_TOKEN.getErrorMessage())) {

            setResponse(response, TokenErrorCode.WRONG_TYPE_TOKEN);

        }
        //토큰 만료된 경우
        else if (exception.equals(TokenErrorCode.EXPIRED_TOKEN.getErrorMessage())) {
            setResponse(response, TokenErrorCode.EXPIRED_TOKEN);

        }
        //지원되지 않는 토큰인 경우
        else if (exception.equals(TokenErrorCode.UNSUPPORTED_TOKEN.getErrorMessage())) {

            setResponse(response, TokenErrorCode.UNSUPPORTED_TOKEN);
        } else {

            setResponse(response, TokenErrorCode.ACCESS_DENIED);

        }
    }

    private void setResponse(HttpServletResponse response, TokenErrorCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", exceptionCode.getErrorMessage());
        responseJson.put("code", exceptionCode.getErrorCode());

        response.getWriter().print(responseJson);
    }
}
