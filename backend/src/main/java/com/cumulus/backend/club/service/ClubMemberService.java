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

    // 클럽멤버인지 확인
    public ClubMember getClubMemberOrThrow(Long userId, Long clubId) {
        return clubMemberRepository.findByUserIdAndClubId(userId, clubId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_CLUB_MEMBER));
    }

    // 클럽리더인지 확인
    public void checkClubLeaderOrThrow(Long userId, Long clubId) {
        ClubMember leaderMember = clubMemberRepository.findByClubIdAndRole(clubId, MemberRole.LEADER)
                .orElseThrow(()-> new CustomException(ErrorCode.NO_PERMISSION_CLUB));
        if(!leaderMember.getUser().getId().equals(userId)) throw new CustomException(ErrorCode.USER_NOT_CLUB_LEADER);
    }
}
