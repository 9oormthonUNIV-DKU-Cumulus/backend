package com.cumulus.backend.security.handler;

import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.exception.ErrorResponder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ErrorResponder errorResponder;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        log.warn("JsonAuthenticationEntryPoint - AuthenticationException : {}", authException.getMessage());
        errorResponder.sendError(response, ErrorCode.NOT_LOGIN_USER, authException);
    }
}
