package com.cumulus.backend.activity.controller;

import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.ActivityApplicationCreateDto;
import com.cumulus.backend.activity.dto.ActivityApplicationForm;
import com.cumulus.backend.activity.service.ActivityApplicationService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.security.jwt.JwtUtil;
import com.cumulus.backend.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityApplicationController {

    private final JwtUtil jwtUtil;
    private final ActivityApplicationService activityApplicationService;

    @GetMapping("/{id}/apply-form")
    public ResponseEntity<ApiResponse<?>> getApplicationForm( HttpServletRequest request ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);
        User user = activityApplicationService.loadUserInfoForApplicationForm(userId);
        ActivityApplicationForm activityApplicationForm = new ActivityApplicationForm(
                user.getUserName(),
                user.getPhoneNumber() == null ? "" : user.getPhoneNumber(),
                user.getMajor() == null ? "" : user.getMajor()
        );
        return ResponseEntity.ok(ApiResponse.success(activityApplicationForm));
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<ApiResponse<?>> postApplication(
            HttpServletRequest request, @PathVariable("id") Long activityId,
            ActivityApplicationCreateDto applicationCreateDto
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);
        activityApplicationService.createActivityApplication(activityId, userId, applicationCreateDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
