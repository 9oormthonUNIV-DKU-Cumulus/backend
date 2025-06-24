package com.cumulus.backend.user.controller;

import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.user.dto.EmailRequest;
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
    public ResponseEntity<ApiResponse<?>> sendEmailCode(@Valid @RequestBody EmailRequest emailRequest){
        emailService.sendCodeToEmail(emailRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
