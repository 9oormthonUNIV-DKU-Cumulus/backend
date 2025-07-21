package com.cumulus.backend.club.dto;

import com.cumulus.backend.club.domain.Club;
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

    public static ClubSummaryDto fromEntity(Club club) {
        return ClubSummaryDto.builder()
                .id(club.getId())
                .clubName(club.getClubName())
                .clubDesc(club.getClubDesc())
                .category(club.getCategory().name())
                .campus(club.getCampus().name())
                .memberCount(club.getMembers().size())
                .build();
    }
}
