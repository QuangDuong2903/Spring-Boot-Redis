package com.quangduong.redis.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quangduong.redis.payload.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class AuthExceptionHandler implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logger.error(authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = new ObjectMapper().writeValueAsString(new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", request.getRequestURI()));
        response.getWriter()
                .print(json);
    }
}
