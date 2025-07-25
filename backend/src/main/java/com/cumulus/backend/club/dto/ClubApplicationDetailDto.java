package com.cumulus.backend.club.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClubApplicationDetailDto {
    private Long clubApplicationId;
    private String clubName;
    private String clubDesc;
    private String campus;
    private String category;
    private LocalDateTime createdAt;
    private String applicationStatus;
}
