package com.cumulus.backend.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LeaderDto {
    private Long memberId;
    private String memberName;
    private String profileImgUrl;
}
