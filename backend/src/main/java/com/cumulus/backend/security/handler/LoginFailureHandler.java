package com.cumulus.backend.security.handler;

import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.exception.ErrorResponder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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

        if (exception instanceof InternalAuthenticationServiceException &&
                exception.getCause() instanceof UsernameNotFoundException ) {
            log.warn("LoginFailureHandler - USER_EMAIL_NOT_FOUND");
            errorResponder.sendError(response, ErrorCode.USER_EMAIL_NOT_FOUND, exception.getCause());
            return;
        }

        if (exception instanceof UsernameNotFoundException) {
            errorResponder.sendError(response, ErrorCode.USER_EMAIL_NOT_FOUND, exception);
            return;
        }

        if (exception instanceof BadCredentialsException) {
            log.warn("LoginFailureHandler - BadCredentialsException: {}", exception.getMessage());
            errorResponder.sendError(response, ErrorCode.INVALID_PASSWORD, exception);
            return;
        }

        log.warn("LoginFailureHandler - Unknown Authentication Error: {}", exception.getMessage());
        errorResponder.sendError(response, ErrorCode.AUTH_UNKNOWN_ERROR, exception);
    }
}
