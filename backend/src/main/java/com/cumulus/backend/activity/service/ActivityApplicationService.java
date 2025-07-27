package com.cumulus.backend.activity.service;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.ActivityApplicationCreateRequestDto;
import com.cumulus.backend.activity.repository.ActivityApplicationRepository;
import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ActivityApplicationService{

    private final ActivityApplicationRepository activityApplicationRepository;
    private final ActivityService activityService;

    @Transactional
    public ActivityApplication createActivityApplication(Activity activity, ClubMember clubMembership,
                                          ActivityApplicationCreateRequestDto applicationCreateDto) {
        //중복신청제어
        boolean alreadyApplied = activityApplicationRepository
                .existsByActivityAndApplicant(activity, clubMembership);
        if (alreadyApplied) {
            throw new CustomException(ErrorCode.DUPLICATE_ACTIVITY_APPLICATION);
        }

        ActivityApplication activityApplication = ActivityApplication.builder()
                            .activity(activity)
                            .applicant(clubMembership)
                            .applicationText(applicationCreateDto.getApplicationText())
                            .createdAt(LocalDateTime.now())
                            .applyUserName(applicationCreateDto.getUserName())
                            .applyUserPhoneNumber(applicationCreateDto.getPhoneNumber())
                            .applyUserMajor(applicationCreateDto.getMajor())
                            .build();

        ActivityApplication savedApplication = activityApplicationRepository.save(activityApplication);
        log.info("모임신청 등록완료 - 신청id{}", savedApplication.getId());
        activity.setNowParticipants(activity.getNowParticipants() + 1);
        return savedApplication;
    }

    public List<ActivityApplication> getActivityApplications(Long userId, Long activityId) {
        if(!activityService.checkActivtiyHostingUser(activityId, userId)){
            log.error("모임신청내역 조회권한 없음");
            throw new CustomException(ErrorCode.NO_PERMISSION_ACTIVITY_APPLICATION);
        }

        Activity activity = activityService.findById(activityId);
        return activityApplicationRepository.findByActivity(activity);
    }

    @Transactional
    public void deleteActivityApplication(Activity activity, Long activtiyApplicationId,
                                          ClubMember clubMembership) {

        ActivityApplication application = activityApplicationRepository.findOne(activtiyApplicationId)
                .orElseThrow(()-> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));

        if(!application.getApplicant().equals(clubMembership)){
            log.error("모임신청내역 삭제권한 없음");
            throw new CustomException(ErrorCode.NO_PERMISSION_ACTIVITY_APPLICATION);
        }

        activityApplicationRepository.delete(application);
        log.info("모임신청 삭제완료");
        activity.setNowParticipants(activity.getNowParticipants() - 1);
    }
}
