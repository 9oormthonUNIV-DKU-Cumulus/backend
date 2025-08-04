package com.cumulus.backend.like.service;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.service.ActivityService;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.service.ClubService;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.like.domain.Like;
import com.cumulus.backend.like.repository.LikeRepository;
import com.cumulus.backend.user.domain.User;
import com.cumulus.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {

    private final UserService userService;
    private final ActivityService activityService;
    private final ClubService clubService;
    private final LikeRepository likeRepository;

    @Transactional
    public Like createClubLike(Long userId, Long clubId){
        User user = userService.findById(userId);
        Club club = clubService.findById(clubId);

        Like like = Like.builder()
                .user(user)
                .club(club)
                .activity(null)
                .build();
        like.validateTarget();

        return likeRepository.save(like);
    }

    public boolean checkClubLike(Long userId, Long clubId){
        return likeRepository.existsByUserIdAndClubId(userId, clubId);
    }
}
