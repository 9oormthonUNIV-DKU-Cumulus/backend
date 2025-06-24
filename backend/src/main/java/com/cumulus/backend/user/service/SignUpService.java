package com.cumulus.backend.user.service;

import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.dto.SignUpRequest;
import com.cumulus.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

        redisTemplate.delete(key);
    }

    // 회원가입 정보의 유효성 확인
    private void validateSignUpRequest(SignUpRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
        }
    }

    // 메일인증 여부의 확인
    private void validateEmailAuthenticated(String key) {
        String verified = redisTemplate.opsForValue().get(key);
        if (!"true".equals(verified)) throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
    }
}
