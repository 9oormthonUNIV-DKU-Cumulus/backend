package com.cumulus.backend.activity.dto;

import com.cumulus.backend.activity.domain.ActivityApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActivityApplicationDto {
    private Long id;
    private String userName;
    private String applicationText;
    private LocalDateTime createdAt;

    public static ActivityApplicationDto fromEntity(ActivityApplication activityApplication){
        return new ActivityApplicationDto(
                activityApplication.getId(),
                activityApplication.getApplyUserName(),
                activityApplication.getApplicationText(),
                activityApplication.getCreatedAt()
        );
    }
}
