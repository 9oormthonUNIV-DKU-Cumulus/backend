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

    private void validateSignUpRequest(SignUpRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void registerUser(SignUpRequest request){
        validateSignUpRequest(request);

        User savedUser = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .major(request.getMajor())
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepository.save(savedUser);
    }
}
