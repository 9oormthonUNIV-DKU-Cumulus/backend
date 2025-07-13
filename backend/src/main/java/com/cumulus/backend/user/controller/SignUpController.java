package com.cumulus.backend.user.controller;

import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.user.dto.EmailSendRequest;
import com.cumulus.backend.user.dto.EmailVerifyRequest;
import com.cumulus.backend.user.dto.SignUpRequest;
import com.cumulus.backend.user.service.EmailService;
import com.cumulus.backend.user.service.SignUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag( name = "가입", description = "가입자 인증 및 가입처리, permitAll 엔드포인트")
public class SignUpController {

    private final SignUpService signUpService;
    private final EmailService emailService;

    @PostMapping("/signup")
    @Operation(summary = "유저가입처리", description = "가입유저정보의 유효성 및 비중복 & 이메일인증처리여부 확인하여 가입처리")
    public ResponseEntity<ApiResponse<?>> signUpRequest(@Valid @RequestBody SignUpRequest signUpRequest){
        signUpService.registerUser(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success("[INFO] 회원가입정상처리 : 유효성검사 통과-> 메일인증 통과"));
    }

    @PostMapping("/email-code/send")
    @Operation(summary = "이메일 인증코드 전송요청",
            description = "학교계정 이메일주소로 6자리 랜덤 인증코드를 전송(인증코드 유효시간 5분)")
    public ResponseEntity<ApiResponse<?>> sendEmailCode(@Valid @RequestBody EmailSendRequest emailSendRequest){
        emailService.sendCodeToEmail(emailSendRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.success("[INFO] 이메일코드 발송완료 & 레디스 저장완료"));
    }

    @PostMapping("/email-code/verify")
    @Operation(summary = "이메일 인증코드 전송요청",
            description = "학교계정 이메일로 전달받은 인증코드를 검증(이후 인증완료 상태저장은 30분간 유효)")
    public ResponseEntity<ApiResponse<?>> verifyCode(@Valid @RequestBody EmailVerifyRequest emailVerifyRequest){
        emailService.verifyCode(emailVerifyRequest.getEmail(), emailVerifyRequest.getCode());
        return ResponseEntity.ok(ApiResponse.success("[INFO] 이메일코드 검증완료 & 레디스 삭제완료"));
    }
}
