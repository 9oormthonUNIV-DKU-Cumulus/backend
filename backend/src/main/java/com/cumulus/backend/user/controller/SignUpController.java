package com.cumulus.backend.user.controller;

import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.dto.EmailSendRequest;
import com.cumulus.backend.user.dto.EmailVerifyRequest;
import com.cumulus.backend.user.dto.SignUpRequest;
import com.cumulus.backend.user.service.EmailService;
import com.cumulus.backend.user.service.SignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signUpRequest(@Valid @RequestBody SignUpRequest signUpRequest){
        signUpService.registerUser(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/email-code/send")
    public ResponseEntity<ApiResponse<?>> sendEmailCode(@Valid @RequestBody EmailSendRequest emailSendRequest){
        emailService.sendCodeToEmail(emailSendRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/email-code/verify")
    public ResponseEntity<ApiResponse<?>> verifyCode(@Valid @RequestBody EmailVerifyRequest emailVerifyRequest){
        boolean isValid = emailService.verifyCode(emailVerifyRequest.getEmail(), emailVerifyRequest.getCode());
        if (!isValid) throw new CustomException(ErrorCode.EMAIL_VERIFICATION_CODE_INVALID);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
