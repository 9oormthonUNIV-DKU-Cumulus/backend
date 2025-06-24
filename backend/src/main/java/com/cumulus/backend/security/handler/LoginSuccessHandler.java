package com.cumulus.backend.security.handler;

import com.cumulus.backend.exception.ApiResponder;
import com.cumulus.backend.security.jwt.JwtUtil;
import com.cumulus.backend.user.domain.CustomUserDetails;
import com.cumulus.backend.user.dto.LoginResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ApiResponder apiResponder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        // 인증성공한 인증객체로부터 userId, authorities 추출
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String accessToken = jwtUtil.createAccessToken(userId, authorities);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);
        apiResponder.sendSuccess(response, loginResponse);
    }
}
