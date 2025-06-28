package com.cumulus.backend.activity.service;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.activity.dto.*;
import com.cumulus.backend.activity.repository.ActivityApplicationRepository;
import com.cumulus.backend.activity.repository.ActivityRepository;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.repository.ClubRepository;
import com.cumulus.backend.common.ApplyStatus;
import com.cumulus.backend.common.Category;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.repository.UserRepository;
import com.cumulus.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.cumulus.backend.exception.ErrorCode.CLUB_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserService userService;
    private final ActivityApplicationRepository activityApplicationRepository;

    public Activity findById(Long activityId){
        return activityRepository.findOne(activityId)
                .orElseThrow(()-> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
    }

    @Transactional
    public Activity createActivity(ActivityCreateRequestDto activityDto, Long userId) {
        Category category = Category.fromId(activityDto.getCategoryId());
        LocalDateTime createdAt = LocalDateTime.now();
        User hostingUser = userRepository.findOne(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        Activity.ActivityBuilder builder = Activity.builder()
                .title(activityDto.getTitle())
                .category(category)
                .description(activityDto.getDescription())
                .meetingDate(activityDto.getMeetingDate())
                .deadline(activityDto.getDeadline())
                .createdAt(createdAt)
                .maxParticipants(activityDto.getMaxParticipants())
                .nowParticipants(0)
                .hostingUser(hostingUser);

        // 동아리 모임일 경우에만 설정
        if(activityDto.getClubId() != null){
            Club club = clubRepository.findOne(activityDto.getClubId())
                    .orElseThrow(() -> new CustomException(CLUB_NOT_FOUND));
            builder
                    .club(club)
                    .isPrivate(true); // 동아리 모임 비공개처리
        }

        Activity savedActivity = activityRepository.save(builder.build());
        log.info("activity:{} - 새 모임이 정상적으로 등록되었습니다.", savedActivity.getId());

        return savedActivity ;
    }

    public ActivityDetailDto getActivity(Long activityId) {
        Activity activity = findById(activityId);

        return new ActivityDetailDto(
          activity.getId(), activity.getTitle(), activity.getMeetingDate(), activity.getDeadline(),
          activity.getMaxParticipants(), activity.getNowParticipants(), activity.getDescription()
        );
    }

    @Transactional
    public void updateActivity(Long activityId, ActivityUpdateRequestDto activityDto, Long userId) {
        Activity activity = findById(activityId);

        // 주최자 본인인지 확인
        if(!activity.getHostingUser().getId().equals(userId)){
            log.error("모임수정권한 없음 - 모임주최자:{}, 수정접근자:{}",activity.getHostingUser().getId(),userId);
            throw new CustomException(ErrorCode.NO_PERMISSION_ACTIVITY);
        }

        if( activityDto.getTitle() != null ) activity.setTitle( activityDto.getTitle() );
        if( activityDto.getCategoryId() != null ) activity.setCategory( Category.fromId(activityDto.getCategoryId()) );
        if( activityDto.getDescription() != null ) activity.setDescription( activityDto.getDescription() );
        if( activityDto.getMeetingDate() != null ) activity.setMeetingDate( activityDto.getMeetingDate() );
        if( activityDto.getDeadline() != null ) activity.setDeadline( activityDto.getDeadline() );
        if( activityDto.getMaxParticipants() != null ) activity.setMaxParticipants(activityDto.getMaxParticipants() );

        log.info("activity:{} - 모임이 정상적으로 수정되었습니다.", activityId);
    }

    @Transactional
    public void deleteActivity(Long activityId, Long userId) {
        Activity activity = findById(activityId);

        // 주최자 본인인지 확인
        if(!activity.getHostingUser().getId().equals(userId)){
            log.error("모임삭제권한 없음 - 모임주최자:{}, 수정접근자:{}",activity.getHostingUser().getId(),userId);
            throw new CustomException(ErrorCode.NO_PERMISSION_ACTIVITY);
        }

        activityRepository.delete(activity);
        log.info("activity:{} - 모임이 정상적으로 삭제되었습니다.", activityId);
    }

    public ActivityListDto getActivityList(ActivitySearchRequestDto activitySearchRequestDto) {
        Category category = Category.fromId(activitySearchRequestDto.getCategoryId());
        if(activitySearchRequestDto.getSort()==null) activitySearchRequestDto.setSort("latest");
        List<Activity> activities = activityRepository.search(activitySearchRequestDto, category);

        List<ActivityDetailDto> activityDtos = activities.stream()
                .map(ActivityDetailDto::fromEntityWithoutDescription)
                .collect(Collectors.toList());

        return new ActivityListDto(activityDtos);
    }

    public ActivityListDto getActivityHosting(Long userId) {
        User user = userService.findById(userId);
        List<Activity> activities = activityRepository.findByHostingUser(user);
        List<ActivityDetailDto> activityDtos = activities.stream()
                .map(ActivityDetailDto::fromEntityWithoutDescription)
                .collect(Collectors.toList());

        return new ActivityListDto(activityDtos);
    }

    public ActivityListDto getActivityWithStatus(Long userId, ApplyStatus applyStatus) {
        User user = userService.findById(userId);
        List<ActivityApplication> applications = activityApplicationRepository.findByUserAndStatus(user, applyStatus);
        List<Activity> activities = applications.stream()
                .map(ActivityApplication::getActivity)
                .toList();
        List<ActivityDetailDto> activityDtos = activities.stream()
                .map(ActivityDetailDto::fromEntityWithoutDescription)
                .collect(Collectors.toList());
        return new ActivityListDto(activityDtos);
    }
}
