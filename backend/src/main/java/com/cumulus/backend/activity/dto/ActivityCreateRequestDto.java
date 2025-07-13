package com.cumulus.backend.activity.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityCreateRequestDto {
    @NotNull(message = "소속 동아리값은 반드시 입력해야합니다.")
    private Long clubId;

    @NotBlank(message = "모임글 제목은 반드시 입력해야합니다.")
    private String title;

    @NotBlank(message = "모임글 설명글 반드시 입력해야합니다.")
    private String description;

    @NotNull(message = "모임일자 반드시 입력해야합니다.")
    @Future(message = "모임일자는 반드시 미래여야 합니다.")
    private LocalDateTime meetingDate; // 모임일자

    @NotNull(message = "신청마감일자 반드시 입력해야합니다.")
    @Future(message = "신청 마감일자는 반드시 미래여야 합니다.")
    private LocalDateTime deadline; // 신청마감일자

    @NotNull(message = "최대인원수 반드시 입력해야합니다.")
    @Min(value = 1, message = "모집인원수는 최대 한명이상이 되어야합니다.")
    private Integer maxParticipants;
}
