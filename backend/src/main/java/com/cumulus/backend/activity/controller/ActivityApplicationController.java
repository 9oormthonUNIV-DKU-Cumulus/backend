package com.cumulus.backend.activity.controller;

import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.ActivityApplicationCreateRequestDto;
import com.cumulus.backend.activity.dto.ActivityApplicationDto;
import com.cumulus.backend.activity.dto.ActivityApplicationForm;
import com.cumulus.backend.activity.service.ActivityApplicationService;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.common.ApiResponse;
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

    @GetMapping("/{id}/apply-form")
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

    @PostMapping("/{id}/apply")
    public ResponseEntity<ApiResponse<?>> postApplication(
            HttpServletRequest request, @PathVariable("id") Long activityId,
            @RequestBody @Valid ActivityApplicationCreateRequestDto applicationCreateDto
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);
        activityApplicationService.createActivityApplication(activityId, userId, applicationCreateDto);
        return ResponseEntity.ok(ApiResponse.success("모임신청 등록되었습니다."));
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<ApiResponse<?>> getApplication(
            HttpServletRequest request, @PathVariable("id") Long activityId
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        List<ActivityApplication> activityApplications = activityApplicationService.getActivityApplications(userId, activityId);

        List<ActivityApplicationDto> result = activityApplications.stream()
                .map(ActivityApplicationDto::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
