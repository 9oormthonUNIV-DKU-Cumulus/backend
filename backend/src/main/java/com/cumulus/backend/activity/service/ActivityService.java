package com.cumulus.backend.activity.service;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.dto.ActivityCreateDto;
import com.cumulus.backend.activity.repository.ActivityRepository;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.repository.ClubRepository;
import com.cumulus.backend.common.Category;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.cumulus.backend.exception.ErrorCode.CLUB_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public Activity createActivity(ActivityCreateDto activityDto, Long userId) {
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
}
