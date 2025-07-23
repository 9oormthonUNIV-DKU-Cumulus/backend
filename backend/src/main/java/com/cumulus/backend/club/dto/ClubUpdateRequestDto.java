package com.cumulus.backend.club.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ClubUpdateRequestDto {
    private String clubName;
    private String clubDesc;

    @Pattern(regexp = "JUKJEON|CHEONAN",
            message = "캠퍼스 선택은 JUKJEON, CHEONAN 중 하나여야 합니다.")
    private String campus;
}
