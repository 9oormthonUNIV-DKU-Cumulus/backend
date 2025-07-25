package com.cumulus.backend.activity.controller;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.dto.ActivityCreateRequestDto;
import com.cumulus.backend.activity.dto.ActivityDetailDto;
import com.cumulus.backend.activity.dto.ActivityUpdateRequestDto;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.club.service.ClubMemberService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.security.jwt.JwtUtil;
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
@RequestMapping("/api/activity")
@Tag(name = "모임")
public class ActivityController {

    private final ActivityService activityService;
    private final JwtUtil jwtUtil;
    private final ClubMemberService clubMemberService;

    @PostMapping
    @Operation(summary = "모임등록", description = "동아리내 새로운 모임등록")
    public ResponseEntity<ApiResponse<?>> postActivity(
            @RequestBody @Valid ActivityCreateRequestDto activityDto, HttpServletRequest request
    ) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        Long clubId = activityDto.getClubId();
        ClubMember clubMember = clubMemberService.getClubMemberOrThrow(userId, clubId);

        Activity savedActivity = activityService.createActivity(activityDto, clubMember);
        return ResponseEntity.ok(ApiResponse.success(savedActivity.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정모임 상세조회", description = "특정 1개의 모임에 대한 상세조회")
    public ResponseEntity<ApiResponse<?>> getActivity(
            @Parameter(description = "모임Id", required = true) @PathVariable("id") Long activityId
    ) {
        ActivityDetailDto activityDetailDto = activityService.getActivity(activityId);
        return ResponseEntity.ok(ApiResponse.success(activityDetailDto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "특정모임 수정", description = "모임주최자 권한을 가진경우 모임에 대한 수정")
    public ResponseEntity<ApiResponse<?>> patchActivity(
            @Parameter(description = "모임Id", required = true) @PathVariable("id") Long activityId,
            @RequestBody @Valid ActivityUpdateRequestDto activityDto,
            HttpServletRequest request
    ) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        Long clubId = activityDto.getClubId();
        ClubMember clubMember = clubMemberService.getClubMemberOrThrow(userId, clubId);

        activityService.updateActivity(activityId, activityDto, clubMember);
        return ResponseEntity.ok(ApiResponse.success("모임이 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "특정모임 삭제", description = "모임주최자 권한을 가진경우 모임에 대한 삭제")
    public ResponseEntity<ApiResponse<?>> deleteActivity(
            @Parameter(description = "모임Id", required = true) @PathVariable("id") Long activityId,
            @Parameter(description = "삭제할 모임의 clubID조회") @RequestParam Long clubId,
            HttpServletRequest request
    ) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);
        ClubMember clubMember = clubMemberService.getClubMemberOrThrow(userId, clubId);

        activityService.deleteActivity(activityId, clubMember);
        return ResponseEntity.ok(ApiResponse.success("모임이 삭제되었습니다."));
    }
}
