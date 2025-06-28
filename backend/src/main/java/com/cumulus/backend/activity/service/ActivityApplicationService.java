package com.cumulus.backend.activity.service;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.ActivityApplicationCreateDto;
import com.cumulus.backend.activity.repository.ActivityApplicationRepository;
import com.cumulus.backend.activity.repository.ActivityRepository;
import com.cumulus.backend.common.ApplicationStatus;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.repository.UserRepository;
import com.cumulus.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityApplicationService{

    private final ActivityApplicationRepository activityApplicationRepository;
    private final ActivityService activityService;
    private final UserService userService;

    @Transactional
    public ActivityApplication createActivityApplication(Long activityId, Long userId,
                                          ActivityApplicationCreateDto applicationCreateDto) {
        Activity activity = activityService.findById(activityId);
        User user = userService.findById(userId);

        ActivityApplication activityApplication = ActivityApplication.builder()
                            .activity(activity)
                            .user(user)
                            .applicationText(applicationCreateDto.getApplicationText())
                            .createdAt(LocalDateTime.now())
                            .applicationStatus(ApplicationStatus.PENDING)
                            .applyUserName(applicationCreateDto.getUserName())
                            .applyUserPhoneNumber(applicationCreateDto.getPhoneNumber())
                            .applyUserMajor(applicationCreateDto.getMajor())
                            .build();

        return activityApplicationRepository.save(activityApplication);
    }
}
