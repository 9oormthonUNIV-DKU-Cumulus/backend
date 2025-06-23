package com.cumulus.backend.security.handler;

import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.exception.ErrorResponder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {
    private final ErrorResponder errorResponder;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof UsernameNotFoundException){
            // loadUserByUsername인증에서 식별자로 사용자를 찾지 못함
            log.warn("LoginFailureHandler - UsernameNotFoundException : {}", exception.getMessage());
            errorResponder.sendError(response, ErrorCode.USER_EMAIL_NOT_FOUND, exception);
        } else if(exception instanceof BadCredentialsException){
            // loadUserByUsername인증에서 비밀번호 불일치
            log.warn("LoginFailureHandler - BadCredentialsException : {}", exception.getMessage());
            errorResponder.sendError(response, ErrorCode.INVALID_PASSWORD, exception);
        } else {
            // loadUserByUsername에서 런타임 예외 발생
            log.warn("LoginFailureHandler - InternalAuthenticationServiceException : {}");
            errorResponder.sendError(response, ErrorCode.AUTH_UNKNOWN_ERROR, exception);
        }
    }
}
