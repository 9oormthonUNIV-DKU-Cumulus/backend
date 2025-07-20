package com.cumulus.backend.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubSummaryDto {
    private Long id;
    private String clubName;
    private String clubDesc;
    private String category;
    private String campus;
    private int memberCount;
}
