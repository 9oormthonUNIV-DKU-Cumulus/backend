package com.cumulus.backend.club.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ClubDetailDto {
    private Long id;
    private String clubName;
    private String clubDesc;
    private String category;
    private String campus;
    private int memberCount;

    private LeaderDto leader;
    private List<MemberDto> members;
}
