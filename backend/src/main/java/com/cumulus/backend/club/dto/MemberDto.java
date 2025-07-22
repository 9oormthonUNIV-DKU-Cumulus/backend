package com.cumulus.backend.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {
    private Long memberId;
    private String name;
    private String profileImageUrl;
}
