package com.cumulus.backend.activity.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityUpdateRequestDto {
    @NotBlank(message = "수정할 모임의 동아리id는 반드시 입력해야합니다.")
    private Long clubId;

    private String title;

    private String description;

    @Future(message = "모임일자는 반드시 미래여야 합니다.")
    private LocalDateTime meetingDate; // 모임일자

    @Future(message = "신청 마감일자는 반드시 미래여야 합니다.")
    private LocalDateTime deadline; // 신청마감일자

    @Min(value = 1, message = "모집인원수는 최대 한명이상이 되어야합니다.")
    private Integer maxParticipants;
}
