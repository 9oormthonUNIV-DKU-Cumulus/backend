package com.cumulus.backend.club.controller;

import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.dto.ClubCreateRequestDto;
import com.cumulus.backend.club.service.ClubService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.security.jwt.JwtUtil;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
@Tag(name="동아리")
public class ClubController {

    private final JwtUtil jwtUtil;
    private final ClubService clubService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "동아리 등록", description = "새로운 동아리 등록")
    public ResponseEntity<ApiResponse<?>> postClub(
            @RequestBody @Valid ClubCreateRequestDto clubtDto, HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);
        User user = userService.findById(userId);

        Club club = clubService.createWithLeader(user, clubtDto);
        return ResponseEntity.ok(ApiResponse.success(club.getId()));
    }
}
