package com.cumulus.backend.user.controller;

import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.security.jwt.JwtUtil;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.dto.LoginResponse;
import com.cumulus.backend.user.dto.ReissueRequest;
import com.cumulus.backend.user.repository.UserRepository;
import com.cumulus.backend.user.service.SignUpService;
import com.cumulus.backend.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;
    private final UserService userService;
    private final SignUpService signUpService;

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<?>> reissueToken(@RequestBody @Valid ReissueRequest reissueRequest){
        String refreshToken = reissueRequest.getRefreshToken();
        
        jwtUtil.validateRefreshToken(refreshToken); // 토큰 유효성 확인 -> jwtExecption 자체처리
        Long userId = jwtUtil.extractUserId(refreshToken, true); // 유저Id 추출

        // 저장된 refreshToken 검증
        String savedRefreshToken = redisTemplate.opsForValue().get("refreshToken:" + userId);
        if( savedRefreshToken==null ){ // 만료되어 삭제된 refreshtoken, 로그아웃한 유저
            log.error("[REISSUE] userId={} - 저장된 refreshToken 없음 - 만료된경우, 로그아웃한 경우", userId);
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED); // 클라이언트에 다시 로그인 요청
        }
        if( !savedRefreshToken.equals(refreshToken) ){
            log.error("[REISSUE] userId={} - 저장된 refreshToken과 불일치", userId);
            throw new CustomException(ErrorCode.MISMATCHED_REFRESH_TOKEN); // 불일치
        }
        
        // 유저를 조회하여 accessToken 생성
        User user = userService.findById(userId);
        List<String> authorities = List.of(user.getRole().name());
        String newAccessToken = jwtUtil.createAccessToken(userId, authorities);

        // refreshToken 만료 임박시 재발급
        Date expiration = jwtUtil.extractExpiration(refreshToken, true);
        long daysLeft = Duration.between(Instant.now(), expiration.toInstant()).toDays();

        String newRefreshToken = refreshToken;
        if(daysLeft < 3){ // 임박 기준 : 3일 미만
            newRefreshToken = jwtUtil.createRefreshToken(userId);
            log.info("[REISSUE] userId={} - refreshToken 재발급 (만료 {}일 남음)", userId, daysLeft);
            try{
                redisTemplate.opsForValue().set("refreshToken:" + userId, newRefreshToken,
                        Duration.ofSeconds(jwtUtil.getRefreshTokenValidityInSeconds()));
            } catch (Exception e){
                log.error("[REISSUE] userId={} - Redis 저장 실패", userId, e);
                throw new CustomException(ErrorCode.REDIS_SAVE_FAIL);
            }
        }

        return ResponseEntity.ok(ApiResponse.success(new LoginResponse(newAccessToken, newRefreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);
        signUpService.logout(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
