package com.cumulus.backend.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ActivityListDto {
    private List<ActivityDetailDto> activityList;
}
