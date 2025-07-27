package com.cumulus.backend.activity.controller;

import com.cumulus.backend.activity.dto.ActivityListDto;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.service.ClubService;
import com.cumulus.backend.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
@Slf4j
@Tag(name = "모임")
public class ActivityListController {

    private final ActivityService activityService;
    private final ClubService clubService;

    //api/activities?sort={latest/popular/all}
    @Operation(summary = "홈화면 모임조회", description = "정렬옵션에 따른 전체모임 조회(특정 동아리소속X)")
    public ResponseEntity<ApiResponse<?>> getActivityList(
            @Parameter(description = "정렬기준: latest, popular, all", required = false)
            @RequestParam(defaultValue = "latest")
            @Pattern(regexp = "latest|popular|all",
                    message = "정렬 조건은 latest, popular, all 중 하나여야 합니다.") String sort
    ){
        log.info("전체 모임 목록조회 | 필터링 조건 - sort: {}", sort);
        ActivityListDto activityListDto = activityService.getActivityListWithSort(sort);
        return ResponseEntity.ok(ApiResponse.success(activityListDto));
    }

    @GetMapping("/{clubId}")
    @Operation(summary = "동아리내 모임목록 조회", description = "동아리내 전체 모임목록들 조회")
    public ResponseEntity<ApiResponse<?>> getClubActivityList(
        @Parameter(description = "동아리Id", required = true) @PathVariable("clubId") Long clubId
    ){
        log.info("동아리({})내 모임목록조회", clubId);

        Club club = clubService.findById(clubId);
        ActivityListDto activityListDto = activityService.getClubActivityList(club);
        return ResponseEntity.ok(ApiResponse.success(activityListDto));
    }
}
