package com.cumulus.backend.activity.controller;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.ActivityApplicationCreateRequestDto;
import com.cumulus.backend.activity.dto.ActivityApplicationDto;
import com.cumulus.backend.activity.dto.ActivityApplicationForm;
import com.cumulus.backend.activity.service.ActivityApplicationService;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.club.service.ClubMemberService;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
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

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
@Tag(name = "모임신청")
public class ActivityApplicationController {

    private final JwtUtil jwtUtil;
    private final ActivityApplicationService activityApplicationService;
    private final ActivityService activityService;
    private final UserService userService;
    private final ClubMemberService clubMemberService;

    @GetMapping("/apply-form")
    @Operation(summary = "모임신청폼 화면기본정보 전달",
            description = "모임신청폼화면에 사용자 정보 기반 기본정보를 전달 - 사용자이름, 사용자 전화번호, 사용자 전공")
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
    @Operation(summary = "모임신청 등록",
            description = "사용자가 해당 동아리원일 경우를 확인하여 모임신청 처리(중복신청불가), 선착순으로 진행되며 현재인원이 최대인원 이상이되면 참여불가")
    public ResponseEntity<ApiResponse<?>> postApplication(
            HttpServletRequest request,
            @Parameter(description = "모임Id", required = true) @PathVariable("activityId") Long activityId,
            @RequestBody @Valid ActivityApplicationCreateRequestDto applicationCreateDto
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        // 동아리 멤버십 검증
        Activity activity = activityService.findById(activityId);
        Long clubId = activity.getClub().getId();
        ClubMember clubMembership = clubMemberService.getClubMemberOrThrow(userId, clubId);

        // 모임 정원초과 검증
        if(activityService.fullActivtyParticipants(activityId)) throw new CustomException(ErrorCode.ACTIVTIY_FULL);

        activityApplicationService.createActivityApplication(activity, clubMembership, applicationCreateDto);
        return ResponseEntity.ok(ApiResponse.success("모임신청 등록되었습니다."));
    }

    @GetMapping("/{activityId}/applications")
    @Operation(summary = "모임신청내역 상세조회",
            description = "모임의 주최자권한을 가질 경우 모임신청내역을 확인가능")
    public ResponseEntity<ApiResponse<?>> getApplication(
            HttpServletRequest request,
            @Parameter(description = "모임Id", required = true) @PathVariable("activityId") Long activityId
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        List<ActivityApplication> applications =
                activityApplicationService.getActivityApplications(userId, activityId);

        List<ActivityApplicationDto> result = applications.stream()
                .map(ActivityApplicationDto::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/{activityId}/{applicationId}")
    @Operation(summary = "모임신청 삭제",
        description = "마이페이지의 나의 모임 내역 등에서 신청했던 사항을 취소 - 모임을 신청한 유저 본인만 취소가능")
    public ResponseEntity<ApiResponse<?>> deleteApplication(
            HttpServletRequest request,
            @Parameter(description = "모임Id", required = true) @PathVariable("activityId") Long activityId,
            @Parameter(description = "모임신청Id", required = true) @PathVariable("applicationId") Long activtiyApplicationId
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        // 동아리 멤버십 검증
        Activity activity = activityService.findById(activityId);
        Long clubId = activity.getClub().getId();
        ClubMember clubMembership = clubMemberService.getClubMemberOrThrow(userId, clubId);

        activityApplicationService.deleteActivityApplication(activity, activtiyApplicationId, clubMembership);
        return ResponseEntity.ok(ApiResponse.success("모임신청 삭제되었습니다."));
    }
}
