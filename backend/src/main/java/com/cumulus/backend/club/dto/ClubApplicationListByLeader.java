package com.cumulus.backend.club.dto;

import com.cumulus.backend.club.domain.ClubApplication;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClubApplicationListByLeader {
    private Long clubApplicationId;
    private Long clubId;
    private String applicantName;
    private String clubName;
    private String applicantMajor;
    private LocalDateTime createdAt;

    public static ClubApplicationListByLeader fromEntity(ClubApplication clubApplication) {
        return ClubApplicationListByLeader.builder()
                .clubApplicationId(clubApplication.getId())
                .clubId(clubApplication.getClub().getId())
                .applicantName(clubApplication.getUser().getUserName())
                .clubName(clubApplication.getClub().getClubName())
                .applicantMajor(clubApplication.getUser().getMajor())
                .createdAt(clubApplication.getCreatedAt())
                .build();
    }
}
