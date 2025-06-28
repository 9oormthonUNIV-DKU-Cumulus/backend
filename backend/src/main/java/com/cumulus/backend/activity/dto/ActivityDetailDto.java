package com.cumulus.backend.activity.dto;

import com.cumulus.backend.activity.domain.Activity;
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

    public static ActivityDetailDto fromEntity(Activity activity) {
        return new ActivityDetailDto(
                activity.getId(),
                activity.getTitle(),
                activity.getMeetingDate(),
                activity.getDeadline(),
                activity.getMaxParticipants(),
                activity.getNowParticipants(),
                activity.getDescription()
        );
    }

    public static ActivityDetailDto fromEntityWithoutDescription(Activity activity) {
        return new ActivityDetailDto(
                activity.getId(),
                activity.getTitle(),
                activity.getMeetingDate(),
                activity.getDeadline(),
                activity.getMaxParticipants(),
                activity.getNowParticipants(),
                null // description 없이 null로
        );
    }
}
