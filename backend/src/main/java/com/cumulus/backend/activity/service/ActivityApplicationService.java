package com.cumulus.backend.activity.service;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.ActivityApplicationCreateRequestDto;
import com.cumulus.backend.activity.repository.ActivityApplicationRepository;
import com.cumulus.backend.common.ApplyStatus;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityApplicationService{

    private final ActivityApplicationRepository activityApplicationRepository;
    private final ActivityService activityService;
    private final UserService userService;

    @Transactional
    public ActivityApplication createActivityApplication(Long activityId, Long userId,
                                          ActivityApplicationCreateRequestDto applicationCreateDto) {
        Activity activity = activityService.findById(activityId);
        User user = userService.findById(userId);

        ActivityApplication activityApplication = ActivityApplication.builder()
                            .activity(activity)
                            .user(user)
                            .applicationText(applicationCreateDto.getApplicationText())
                            .createdAt(LocalDateTime.now())
                            .applyStatus(ApplyStatus.PENDING)
                            .applyUserName(applicationCreateDto.getUserName())
                            .applyUserPhoneNumber(applicationCreateDto.getPhoneNumber())
                            .applyUserMajor(applicationCreateDto.getMajor())
                            .build();

        ActivityApplication savedApplication = activityApplicationRepository.save(activityApplication);
        log.info("모임신청 등록완료 - 신청id{}", savedApplication.getId());
        return savedApplication;
    }

    public List<ActivityApplication> getActivityApplications(Long userId, Long activityId) {
        Activity activity = activityService.findById(activityId);
        if(!activity.getHostingUser().getId().equals(userId)){
            log.error("모임신청내역 조회권한 없음 - 모임주최자:{}, 조회접근자:{}",activity.getHostingUser().getId(),userId);
            throw new CustomException(ErrorCode.NO_PERMISSION_APPLICATION);
        }
        return activityApplicationRepository.findByActivity(activity);
    }

    @Transactional
    public void updateApplication(Long userId, Long activityId, Long applicationId, ApplyStatus applyStatus) {
        Activity activity = activityService.findById(activityId);
        if(!activity.getHostingUser().getId().equals(userId)){
            log.warn("모임신청 승인권한 없음 - 모임주최자:{}, 조회접근자:{}",activity.getHostingUser().getId(),userId);
            throw new CustomException(ErrorCode.NO_PERMISSION_APPLICATION);
        }

        ActivityApplication application = activityApplicationRepository.findOne(applicationId)
                .orElseThrow(()-> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));

        if(!application.getActivity().getId().equals(activityId)){
            log.warn("신청&모임 불일치 - 신청번호:{}, 모임:{}", applicationId, activityId);
            throw new CustomException(ErrorCode.APPILICATION_ACTIVITY_MISMATCH);
        }

        switch (applyStatus){
            case APPROVE -> application.setApplyStatus(ApplyStatus.APPROVE);
            case REJECT -> application.setApplyStatus(ApplyStatus.REJECT);
        }

        log.info("모임신청상태변경 - 신청({})의 상태: {}", application.getId(), application.getApplyStatus() );
    }
}
