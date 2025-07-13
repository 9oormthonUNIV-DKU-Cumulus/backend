package com.cumulus.backend.club.service;

import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.club.repository.ClubMemberRepository;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;

    public ClubMember getClubMemberOrThrow(Long userId, Long clubId) {
        return clubMemberRepository.findByUserIdAndClubId(userId, clubId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_CLUB_MEMBER));
    }


}
