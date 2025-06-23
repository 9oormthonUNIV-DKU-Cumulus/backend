package com.cumulus.backend.security.filter;

import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.exception.ErrorResponder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionFilter extends OncePerRequestFilter {

    private final ErrorResponder errorResponder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            // Spring Security 내부 Authentication 실패
            log.warn("ExceptionFilter - AuthenticationException : {}", ex.getMessage());
            errorResponder.sendError(response, ErrorCode.NOT_LOGIN_USER, ex);
        } catch (AccessDeniedException ex) {
            // Spring Security 내부 인가 실패
            log.warn("ExceptionFilter - AccessDeniedException : {}", ex.getMessage());
            errorResponder.sendError(response, ErrorCode.ACCESS_DENIED, ex);
        } catch (CustomException ex) {
            // 커스텀 예외
            log.warn("ExceptionFilter - CustomExcetpion : {}", ex.getMessage());
            errorResponder.sendError(response, ex.getErrorCode(), ex);
        } catch (Exception ex) {
            // 기타 모든 예외
            log.warn("ExceptionFilter - Unexpected Exception : {}", ex.getMessage());
            errorResponder.sendError(response, ErrorCode.INTERNAL_SERVER_ERROR, ex);
        }
    }
}
