package com.cumulus.backend.club.dto;

import com.cumulus.backend.club.domain.ClubApplication;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubApplicationListByApplicant {
    private Long clubApplicationId;
    private Long clubId;
    private String clubName;
    private String clubDesc;
    private String campus;
    private String category;
    private String applicationStatus;

    public static ClubApplicationListByApplicant fromEntity(ClubApplication application){
        return ClubApplicationListByApplicant.builder()
                .clubApplicationId(application.getId())
                .clubId(application.getClub().getId())
                .clubName(application.getClub().getClubName())
                .clubDesc(application.getClub().getClubDesc())
                .campus(application.getClub().getCampus().name())
                .category(application.getClub().getCategory().name())
                .applicationStatus(application.getStatus().name())
                .build();
    }
}
