package com.spring.sharepod.security;


import com.spring.sharepod.exception.TokenError.TokenErrorCode;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        TokenErrorCode exceptionCode;
        exceptionCode = TokenErrorCode.ACCESS_DENIED;
        try {
            setResponse(response, exceptionCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setResponse(HttpServletResponse response, TokenErrorCode exceptionCode) throws IOException, JSONException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("errorMessage", exceptionCode.getErrorMessage());
        responseJson.put("errorCode", exceptionCode.getErrorCode());

        response.getWriter().print(responseJson);
    }
}

