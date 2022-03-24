package com.spring.sharepod.security;


import com.spring.sharepod.exception.TokenError.TokenErrorCode;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @SneakyThrows
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        TokenErrorCode exceptionCode;
        exceptionCode = TokenErrorCode.ACCESS_DENIED;
            setResponse(response, exceptionCode);

    }

    @SneakyThrows
    private void setResponse(HttpServletResponse response, TokenErrorCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("errorMessage", exceptionCode.getErrorMessage());
        responseJson.put("errorCode", exceptionCode.getErrorCode());

        response.getWriter().print(responseJson);
    }

}