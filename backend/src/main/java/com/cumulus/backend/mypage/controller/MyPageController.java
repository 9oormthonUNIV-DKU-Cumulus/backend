package com.cumulus.backend.mypage.controller;

import com.cumulus.backend.activity.dto.ActivityListDto;
import com.cumulus.backend.activity.service.ActivityApplicationService;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.mypage.dto.ActivityAndApplicationDto;
import com.cumulus.backend.mypage.dto.UserInfoDto;
import com.cumulus.backend.security.jwt.JwtUtil;
import com.cumulus.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/me")
@RequiredArgsConstructor
@Tag(name = "마이페이지")
public class MyPageController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ActivityService activityService;
    private final ActivityApplicationService activityApplicationService;

    @GetMapping
    @Operation(summary = "유저기본정보 조회", description = "유저 이름, 이메일, 전공, 전화번호 등 기본정보 조회")
    public ResponseEntity<ApiResponse<?>> getMyPage( HttpServletRequest request ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        UserInfoDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @GetMapping("/activities")
    @Operation(summary = "유저개설모임 조회", description = "해당 유저가 개설한 모임을 조회")
    public ResponseEntity<ApiResponse<?>> getHostingActivity( HttpServletRequest request ) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        ActivityListDto activityHosting = activityService.getActivityHosting(userId);
        return ResponseEntity.ok(ApiResponse.success(activityHosting));
    }

    @GetMapping("/activity-applications")
    @Operation(summary = "유저신청 모임조회", description = "해당 유저가 신청한 모임을 조회")
    public ResponseEntity<ApiResponse<?>> getJoinActivity( HttpServletRequest request ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        List<ActivityAndApplicationDto> activityAndApplicationDtos = activityService.getActivityJoin(userId);
        return ResponseEntity.ok(ApiResponse.success(activityAndApplicationDtos));
    }

}
