package com.spring.sharepod.security;

import com.spring.sharepod.exception.TokenError.TokenErrorCode;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String)request.getAttribute("exception");

        System.out.println("exception          " + exception);

        if(exception == null) {
            try {
                setResponse(response, TokenErrorCode.UNKNOWN_ERROR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(TokenErrorCode.WRONG_TYPE_TOKEN.getErrorMessage())) {
            try {
                setResponse(response, TokenErrorCode.WRONG_TYPE_TOKEN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //토큰 만료된 경우
        else if(exception.equals(TokenErrorCode.EXPIRED_TOKEN.getErrorMessage())) {
            try {
                setResponse(response, TokenErrorCode.EXPIRED_TOKEN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //지원되지 않는 토큰인 경우
        else if(exception.equals(TokenErrorCode.UNSUPPORTED_TOKEN.getErrorMessage())) {
            try {
                setResponse(response, TokenErrorCode.UNSUPPORTED_TOKEN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                setResponse(response,TokenErrorCode.ACCESS_DENIED);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setResponse(HttpServletResponse response, TokenErrorCode exceptionCode) throws IOException, JSONException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", exceptionCode.getErrorMessage());
        responseJson.put("code", exceptionCode.getErrorCode());

        response.getWriter().print(responseJson);
    }
}
