package com.cumulus.backend.activity.service;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.ActivityApplicationCreateDto;
import com.cumulus.backend.activity.repository.ActivityRepository;
import com.cumulus.backend.common.ApplicationStatus;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityApplicationService{

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    public User loadUserInfoForApplicationForm(Long userId) {
        User user = userRepository.findOne(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user;
    }

    public ActivityApplication createActivityApplication(Long activityId, Long userId,
                                          ActivityApplicationCreateDto applicationCreateDto) {
        Activity activity = activityRepository.findOne(activityId)
                .orElseThrow(()-> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));

        User user = userRepository.findOne(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

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

        return activityApplication;
    }
}
