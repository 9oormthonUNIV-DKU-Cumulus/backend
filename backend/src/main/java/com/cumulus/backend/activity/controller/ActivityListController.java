package com.cumulus.backend.activity.controller;

import com.cumulus.backend.activity.dto.ActivityListDto;
import com.cumulus.backend.activity.dto.ActivitySearchRequestDto;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ActivityListController {

    private final ActivityService activityService;

    //api/activities?category={id}&sort={latest/popular/all}")
    @GetMapping("api/activities")
    public ResponseEntity<ApiResponse<?>> getActivityList( @Valid @ModelAttribute ActivitySearchRequestDto activitySearchRequestDto){
        log.info("모임목록조회 - query변수 : categoryId={}, 정렬조건={}", activitySearchRequestDto.getCategoryId(), activitySearchRequestDto.getSort());
        ActivityListDto activityListDto = activityService.getActivityList(activitySearchRequestDto);
        return ResponseEntity.ok(ApiResponse.success(activityListDto));
    }
}
