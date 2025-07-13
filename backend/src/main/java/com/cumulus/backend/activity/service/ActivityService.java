package com.cumulus.backend.activity.service;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.*;
import com.cumulus.backend.activity.repository.ActivityApplicationRepository;
import com.cumulus.backend.activity.repository.ActivityRepository;
import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.club.repository.ClubMemberRepository;
import com.cumulus.backend.club.domain.ApplyStatus;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserService userService;
    private final ActivityApplicationRepository activityApplicationRepository;
    private final ClubMemberRepository clubMemberRepository;

    public Activity findById(Long activityId){
        return activityRepository.findOne(activityId)
                .orElseThrow(()-> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
    }

    @Transactional
    public Activity createActivity(ActivityCreateRequestDto activityDto, ClubMember clubMember) {
        LocalDateTime createdAt = LocalDateTime.now();

        Activity activity = Activity.builder()
                .title(activityDto.getTitle())
                .description(activityDto.getDescription())
                .meetingDate(activityDto.getMeetingDate())
                .deadline(activityDto.getDeadline())
                .createdAt(createdAt)
                .maxParticipants(activityDto.getMaxParticipants())
                .nowParticipants(0)
                .hostingUser(clubMember)
                .club(clubMember.getClub())
                .isPrivate(true) // 동아리 모임 비공개 처리
                .build();

        Activity savedActivity = activityRepository.save(activity);
        log.info("cludId:{} - activity:{} - 동아리의 새 모임이 정상적으로 등록되었습니다.",
                savedActivity.getClub().getId(), savedActivity.getId());

        return savedActivity ;
    }

    public ActivityDetailDto getActivity(Long activityId) {
        Activity activity = findById(activityId);
        return ActivityDetailDto.fromEntity(activity);
    }

    @Transactional
    public void updateActivity(Long activityId, ActivityUpdateRequestDto activityDto, ClubMember clubMember) {
        Activity activity = findById(activityId);

        // 주최자 본인인지 확인
        if(!activity.getHostingUser().getId().equals(clubMember.getId())){
            log.error("모임수정권한 없음 - 모임주최자:{}, 수정접근자:{}",activity.getHostingUser().getId(),clubMember.getId());
            throw new CustomException(ErrorCode.NO_PERMISSION_ACTIVITY);
        }

        if( activityDto.getTitle() != null ) activity.setTitle( activityDto.getTitle() );
        if( activityDto.getDescription() != null ) activity.setDescription( activityDto.getDescription() );
        if( activityDto.getMeetingDate() != null ) activity.setMeetingDate( activityDto.getMeetingDate() );
        if( activityDto.getDeadline() != null ) activity.setDeadline( activityDto.getDeadline() );
        if( activityDto.getMaxParticipants() != null ) activity.setMaxParticipants(activityDto.getMaxParticipants() );

        log.info("activity:{} - 모임이 정상적으로 수정되었습니다.", activityId);
    }

    @Transactional
    public void deleteActivity(Long activityId, ClubMember clubMember) {
        Activity activity = findById(activityId);

        // 주최자 본인인지 확인
        if(!activity.getHostingUser().getId().equals(clubMember.getId())){
            log.error("모임삭제권한 없음 - 모임주최자:{}, 수정접근자:{}",activity.getHostingUser().getId(),clubMember.getId());
            throw new CustomException(ErrorCode.NO_PERMISSION_ACTIVITY);
        }

        activityRepository.delete(activity);
        log.info("activity:{} - 모임이 정상적으로 삭제되었습니다.", activityId);
    }

    public ActivityListDto getActivityListWithSort(String sort) {
        List<Activity> activities = activityRepository.search(sort);

        List<ActivityDetailDto> activityDtos = activities.stream()
                .map(ActivityDetailDto::fromEntityWithoutDescription)
                .collect(Collectors.toList());

        return new ActivityListDto(activityDtos);
    }

    // 마이페이지용 - 개설한 모임
    public ActivityListDto getActivityHosting(Long userId) {
        // 유저가 등록된 모든 클럽멤버십 조회
        User user = userService.findById(userId);
        List<ClubMember> clubMemberships = clubMemberRepository.findAllByUser(user);

        // 클럽멤버로 호스팅한 모든 모임조회
        List<ActivityDetailDto> hostingActivities = clubMemberships.stream()
                .flatMap(member -> activityRepository.findByHostingUser(member).stream())
                .map(ActivityDetailDto::fromEntityWithoutDescription)
                .collect(Collectors.toList());

        return new ActivityListDto(hostingActivities);
    }

    // 마이페이지용 - 참여한 모임
    public ActivityListDto getActivityWithStatus(Long userId) {
        // 유저가 등록된 모든 클럽멤버십 조회
        User user = userService.findById(userId);
        List<ClubMember> clubMemberships = clubMemberRepository.findAllByUser(user);

        // 클럽멤버십으로 신청한 모든 모임조회
        List<ActivityApplication> allActivityApplications = clubMemberships.stream()
                .flatMap(member -> activityApplicationRepository.findByApplicant(member).stream())
                .toList();

        // 모임추출
        List<ActivityDetailDto> activityDtos = allActivityApplications.stream()
                .map(ActivityApplication::getActivity)
                .map(ActivityDetailDto::fromEntityWithoutDescription)
                .toList();

        return new ActivityListDto(activityDtos);
    }
}
