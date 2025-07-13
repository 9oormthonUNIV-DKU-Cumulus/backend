package com.cumulus.backend.activity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivitySearchRequestDto {
    @Pattern(regexp = "latest|popular|all", message = "정렬 조건은 'latest', 'popular', 'all' 중 하나여야 합니다.")
    private String sort;
}
