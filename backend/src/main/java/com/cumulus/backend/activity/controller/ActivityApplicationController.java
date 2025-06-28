package com.cumulus.backend.activity.controller;

import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.ActivityApplicationCreateRequestDto;
import com.cumulus.backend.activity.dto.ActivityApplicationDto;
import com.cumulus.backend.activity.dto.ActivityApplicationForm;
import com.cumulus.backend.activity.service.ActivityApplicationService;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.common.ApplyStatus;
import com.cumulus.backend.security.jwt.JwtUtil;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityApplicationController {

    private final JwtUtil jwtUtil;
    private final ActivityApplicationService activityApplicationService;
    private final ActivityService activityService;
    private final UserService userService;

    @GetMapping("/{activityId}/apply-form")
    public ResponseEntity<ApiResponse<?>> getApplicationForm( HttpServletRequest request ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);
        User user = userService.findById(userId);
        ActivityApplicationForm activityApplicationForm = new ActivityApplicationForm(
                user.getUserName(),
                user.getPhoneNumber() == null ? "" : user.getPhoneNumber(),
                user.getMajor() == null ? "" : user.getMajor()
        );
        return ResponseEntity.ok(ApiResponse.success(activityApplicationForm));
    }

    @PostMapping("/{activityId}/apply")
    public ResponseEntity<ApiResponse<?>> postApplication(
            HttpServletRequest request, @PathVariable("activityId") Long activityId,
            @RequestBody @Valid ActivityApplicationCreateRequestDto applicationCreateDto
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);
        activityApplicationService.createActivityApplication(activityId, userId, applicationCreateDto);
        return ResponseEntity.ok(ApiResponse.success("모임신청 등록되었습니다."));
    }

    @GetMapping("/{activityId}/applications")
    public ResponseEntity<ApiResponse<?>> getApplication(
            HttpServletRequest request, @PathVariable("activityId") Long activityId
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        List<ActivityApplication> applications = activityApplicationService.getActivityApplications(userId, activityId);

        List<ActivityApplicationDto> result = applications.stream()
                .map(ActivityApplicationDto::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/applications/{activityId}/{applyId}/approve")
    public ResponseEntity<ApiResponse<?>> patchApplicationApprove(
            HttpServletRequest request,
            @PathVariable("applyId") Long applicationId,
            @PathVariable("activityId") Long activityId
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        activityApplicationService.updateApplication(userId, activityId, applicationId, ApplyStatus.APPROVE);
        return ResponseEntity.ok(ApiResponse.success("모임상태변경 - 승인"));
    }

    @PostMapping("/applications/{activityId}/{applyId}/reject")
    public ResponseEntity<ApiResponse<?>> patchApplicationReject(
            HttpServletRequest request,
            @PathVariable("applyId") Long applicationId,
            @PathVariable("activityId") Long activityId
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        activityApplicationService.updateApplication(userId, activityId, applicationId, ApplyStatus.REJECT);
        return ResponseEntity.ok(ApiResponse.success("모임상태변경 - 거부"));
    }
}
