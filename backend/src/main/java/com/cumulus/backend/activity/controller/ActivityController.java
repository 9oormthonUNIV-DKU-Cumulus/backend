package com.cumulus.backend.activity.controller;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.dto.ActivityCreateDto;
import com.cumulus.backend.activity.dto.ActivityDetailDto;
import com.cumulus.backend.activity.dto.ActivityUpdateDto;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity")
public class ActivityController {

    private final ActivityService activityService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> postActivity(
            @RequestBody @Valid ActivityCreateDto activityDto, HttpServletRequest request ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        Activity savedActivity = activityService.createActivity(activityDto, userId);
        return ResponseEntity.ok(ApiResponse.success(ActivityDetailDto.fromEntity(savedActivity)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getActivity(
            @PathVariable("id") Long activityId, HttpServletRequest request ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        ActivityDetailDto activityDetailDto = activityService.readActivity(activityId);
        return ResponseEntity.ok(ApiResponse.success(activityDetailDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> patchActivity(
            @PathVariable("id") Long activityId,
            @RequestBody @Valid ActivityUpdateDto activityDto,
            HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        activityService.updateActivity(activityId, activityDto, userId);
        return ResponseEntity.ok(ApiResponse.success("모임이 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteActivity(
        @PathVariable("id") Long activityId, HttpServletRequest request ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        activityService.deleteActivity(activityId, userId);
        return ResponseEntity.ok(ApiResponse.success("모임이 삭제되었습니다."));
    }
}
