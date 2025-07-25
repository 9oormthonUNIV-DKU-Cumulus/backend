package com.cumulus.backend.club.controller;

import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.domain.ClubApplication;
import com.cumulus.backend.club.service.ClubApplicationService;
import com.cumulus.backend.club.service.ClubService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.security.jwt.JwtUtil;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/club")
@RequiredArgsConstructor
@Tag(name = "동아리신청")
public class ClubApplicationController {

    private final JwtUtil jwtUtil;
    private final ClubService clubService;
    private final ClubApplicationService clubApplicationService;
    private final UserService userService;

    @PostMapping("/{clubId}/apply")
    @Operation(summary = "동아리신청 등록",
        description = "요청 사용자 동아리 신청을 수행하도록 함(동아리원일경우 신청불가, 중복신청불가), 신청의 초기상태(status): PENDING")
    public ResponseEntity<ApiResponse<?>> applyClub(
            @Parameter(description = "동아리Id", required = true) @PathVariable("clubId") Long clubId,
            HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        Club club = clubService.findById(clubId);
        User user = userService.findById(userId);
        clubApplicationService.createClubApplication(club,user);
        return ResponseEntity.ok(ApiResponse.success("동아리 신청 등록되었습니다."));
    }

    @DeleteMapping("/applications/{applicationId}")
    @Operation(summary = "동아리신청 취소",
        description = "마이페이지의 나의 동아리 신청내역 등에서 신청했던 사항을 취소 - 신청한 유저 본인만 취소가능")
    public ResponseEntity<ApiResponse<?>> deleteApplication(
            @Parameter(description = "동아리신청Id", required = true) @PathVariable("applicationId") Long applicationId,
            HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        ClubApplication clubApplication = clubApplicationService.findById(applicationId);
        User user = userService.findById(userId);
        clubApplicationService.deleteClubApplication(clubApplication,user);
        return ResponseEntity.ok(ApiResponse.success("동아리 신청 취소(삭제)되었습니다."));
    }
}
