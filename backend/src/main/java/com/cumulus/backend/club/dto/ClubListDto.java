package com.cumulus.backend.club.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ClubListDto {
    private List<ClubDetailDto> clubList;
}
