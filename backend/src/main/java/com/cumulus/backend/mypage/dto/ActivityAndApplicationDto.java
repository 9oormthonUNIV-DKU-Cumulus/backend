package com.cumulus.backend.mypage.dto;

import com.cumulus.backend.activity.dto.ActivityDetailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityAndApplicationDto {
    private Long applicationId;
    private ActivityDetailDto activity;
}
