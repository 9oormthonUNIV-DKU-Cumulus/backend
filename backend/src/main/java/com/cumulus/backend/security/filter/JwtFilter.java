package com.cumulus.backend.security.filter;

import com.cumulus.backend.security.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String jwtToken = jwtUtil.resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if ( StringUtils.hasText(jwtToken) && SecurityContextHolder.getContext().getAuthentication() == null ) {
            jwtUtil.validateAccessToken(jwtToken); // 1단계: 토큰 유효성 검증 - 내부에러처리
            Authentication authentication = jwtUtil.getAuthenticationFromAccessToken(jwtToken); // 2단계: 인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다, 요청 uri: {}", authentication.getName(), requestURI);
        } else {
            log.info("해당요청에 유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
