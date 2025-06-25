package com.cumulus.backend.user.controller;

import com.cumulus.backend.common.ApiResponse;
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
        return ResponseEntity.ok(ApiResponse.success("[INFO] 회원가입정상처리 : 유효성검사 통과-> 메일인증 통과"));
    }

    @PostMapping("/email-code/send")
    public ResponseEntity<ApiResponse<?>> sendEmailCode(@Valid @RequestBody EmailSendRequest emailSendRequest){
        emailService.sendCodeToEmail(emailSendRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.success("[INFO] 이메일코드 발송완료 & 레디스 저장완료"));
    }

    @PostMapping("/email-code/verify")
    public ResponseEntity<ApiResponse<?>> verifyCode(@Valid @RequestBody EmailVerifyRequest emailVerifyRequest){
        emailService.verifyCode(emailVerifyRequest.getEmail(), emailVerifyRequest.getCode());
        return ResponseEntity.ok(ApiResponse.success("[INFO] 이메일코드 검증완료 & 레디스 삭제완료"));
    }
}
