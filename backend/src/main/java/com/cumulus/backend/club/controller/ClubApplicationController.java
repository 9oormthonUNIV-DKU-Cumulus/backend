package com.cumulus.backend.club.controller;

import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.domain.ClubApplication;
import com.cumulus.backend.club.dto.ClubApplicationDetailDto;
import com.cumulus.backend.club.dto.ClubApplicationListByApplicant;
import com.cumulus.backend.club.dto.ClubApplicationListByLeader;
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

import java.util.List;

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

    @GetMapping("/applications/applicant")
    @Operation(summary = "동아리 신청내역 목록조회(신청자)",
        description = "마이페이지의 내가 신청한 동아리 신청내역들 확인 - 신청상태확인가능")
    public ResponseEntity<ApiResponse<?>> getApplicationsByApplicant(
            HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        User user = userService.findById(userId);
        List<ClubApplicationListByApplicant> applicationsByApplicant
                = clubApplicationService.getApplicationByApplicant(user);
        return ResponseEntity.ok(ApiResponse.success(applicationsByApplicant));
    }

    @GetMapping("/applicants/leader")
    @Operation(summary = "동아리 신청내역 목록조회(개최자)",
        description = "마이페이지의 내가 개최한 동아리 신청내역들 확인(승인이전 PENDING상태만) -> 추후 신청 승인,거절 여부 선택가능")
    public ResponseEntity<ApiResponse<?>> getApplicationsByLeader(
            HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        User user = userService.findById(userId);
        List<ClubApplicationListByLeader> applicationsByLeader
                = clubApplicationService.getApplicationByLeader(user);
        return ResponseEntity.ok(ApiResponse.success(applicationsByLeader));
    }

    @GetMapping("/applications/{applicationId}")
    @Operation(summary = "동아리신청 상세조회", description = "특정 동아리신청내역에 대한 상세조회")
    public ResponseEntity<ApiResponse<?>> getApplicationDetail(
            @Parameter(description = "동아리신청Id", required = true) @PathVariable("applicationId") Long applicationId
    ){
        ClubApplicationDetailDto applications = clubApplicationService.getApplicationDetail(applicationId);
        return ResponseEntity.ok(ApiResponse.success(applications));
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

    @PostMapping("/applications/{applicationId}/approve")
    @Operation(summary = "동아리 신청 승인",
            description = "마이페이지의 내가 개최한 동아리 신청내역들 -> 승인처리(APPROVE) & 신청자 클럽멤버로 등록")
    public ResponseEntity<ApiResponse<?>> updateApplicationApprove(
            @Parameter(description = "동아리신청Id", required = true) @PathVariable("applicationId") Long applicationId,
            HttpServletRequest request

    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        clubApplicationService.updateApplicationApprove(applicationId, userId);
        return ResponseEntity.ok(ApiResponse.success("동아리 신청 승인처리되었습니다."));
    }

}
