package com.cumulus.backend.mypage.controller;

import com.cumulus.backend.activity.service.ActivityApplicationService;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.mypage.dto.UserInfoDto;
import com.cumulus.backend.security.jwt.JwtUtil;
import com.cumulus.backend.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/me")
@RequiredArgsConstructor
public class MyPageController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ActivityService activityService;
    private final ActivityApplicationService activityApplicationService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getMyPage( HttpServletRequest request ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        UserInfoDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

}
