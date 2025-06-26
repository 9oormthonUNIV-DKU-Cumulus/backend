package com.cumulus.backend.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActivityDetailDto {
    private Long id;
    private String title;
    private LocalDateTime meetingDate;
    private LocalDateTime deadline;
    private int maxParticipants;
    private int nowParticipants;
    private String description;
}
