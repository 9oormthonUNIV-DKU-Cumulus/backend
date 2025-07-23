package com.cumulus.backend.club.service;

import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.club.domain.MemberRole;
import com.cumulus.backend.club.repository.ClubMemberRepository;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;

    public ClubMember getClubMemberOrThrow(Long userId, Long clubId) {
        return clubMemberRepository.findByUserIdAndClubId(userId, clubId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_CLUB_MEMBER));
    }

    public void checkClubLeaderOrThrow(Long userId, Long clubId) {
        ClubMember leaderMember = clubMemberRepository.findByClubIdAndRole(clubId, MemberRole.LEADER)
                .orElseThrow(()-> new CustomException(ErrorCode.CLUB_LEADER_NOT_FOUND));
        if(!leaderMember.getUser().getId().equals(userId)) throw new CustomException(ErrorCode.USER_NOT_CLUB_LEADER);
    }
}
