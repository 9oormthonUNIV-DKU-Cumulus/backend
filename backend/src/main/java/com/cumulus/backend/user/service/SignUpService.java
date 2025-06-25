package com.cumulus.backend.user.service;

import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.dto.SignUpRequest;
import com.cumulus.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public void registerUser(SignUpRequest request){
        validateSignUpRequest(request);

        String key = "email-verified:" + request.getEmail();
        validateEmailAuthenticated(key);

        User savedUser = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .major(request.getMajor())
                .phoneNumber(request.getPhoneNumber())
                .build();
        userRepository.save(savedUser);
        log.info("[SIGNUP] 유저 {} - 가입처리완료", request.getEmail());

        redisTemplate.delete(key);
        log.info("[REDIS] 유저 {} - 인증여부토큰({}) 삭제 완료", request.getEmail(), key);
    }

    // 회원가입 정보의 유효성 확인
    private void validateSignUpRequest(SignUpRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            log.info("[SIGNUP] 유저 {} - 중복가입시도, 이미 존재하는 유저 이메일", request.getEmail());
            throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
        }
        log.info("[SIGNUP] 유저 {} - 가입정보 유효성 확인통과", request.getEmail());

    }

    // 메일인증 여부의 확인
    private void validateEmailAuthenticated(String key) {
        String verified = redisTemplate.opsForValue().get(key);
        if (!"true".equals(verified)) {
            log.info("[REDIS/SIGNUP] 유저 {} - 아직 인증처리를 진행하지 않음", key);
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        log.info("[REDIS/SIGNUP] 유저 인증여부토큰({}) 유효한 상태로 확인", key);
    }
}
