package com.cumulus.backend.club.controller;

import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.dto.ClubCreateRequestDto;
import com.cumulus.backend.club.dto.ClubDetailDto;
import com.cumulus.backend.club.service.ClubService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.security.jwt.JwtUtil;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
@Tag(name="동아리")
public class ClubController {

    private final JwtUtil jwtUtil;
    private final ClubService clubService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "동아리 등록", description = "새로운 동아리 등록, 인증되어 수행하는 유저가 동아리 LEADER가 됩니다.")
    public ResponseEntity<ApiResponse<?>> postClub(
            @RequestBody @Valid ClubCreateRequestDto clubtDto, HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);
        User user = userService.findById(userId);

        Club club = clubService.createWithLeader(user, clubtDto);
        return ResponseEntity.ok(ApiResponse.success(club.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 동아리 상세 조회", description = "특정 1개의 동아리에 대한 상세조회")
    public ResponseEntity<ApiResponse<?>> getClubDetail(
            @Parameter(description = "동아리Id", required = true) @PathVariable("id") Long clubId
    ){
        ClubDetailDto clubDetailDto = clubService.getClub(clubId);
        return ResponseEntity.ok(ApiResponse.success(clubDetailDto));
    }
}
