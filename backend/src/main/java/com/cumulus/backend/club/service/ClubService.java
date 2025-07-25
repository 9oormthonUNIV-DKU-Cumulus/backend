package com.cumulus.backend.club.service;

import com.cumulus.backend.club.domain.*;
import com.cumulus.backend.club.dto.*;
import com.cumulus.backend.club.repository.ClubMemberRepository;
import com.cumulus.backend.club.repository.ClubRepository;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
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
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    public Club findById(Long clubId){
        return clubRepository.findOne(clubId)
                .orElseThrow(()-> new CustomException(ErrorCode.CLUB_NOT_FOUND));
    }

    public List<ClubSummaryDto> getClubListWithFilters(Category category, Campus campus, String sort){
        List<Club> clubs = clubRepository.search(category, campus, sort);

        return clubs.stream()
                .map(ClubSummaryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Club createWithLeader(User user, ClubCreateRequestDto dto){
        Club club = new Club();
        club.setClubName(dto.getClubName());
        club.setClubDesc(dto.getClubDesc());
        club.setCategory(Category.fromId(dto.getCategory()));
        club.setCampus(Campus.fromString(dto.getCampus()));
        club.setCreatedAt(LocalDateTime.now());
        clubRepository.save(club);

        ClubMember leader = new ClubMember();
        leader.setMemberName(user.getUserName());
        leader.setProfileImgUrl(user.getProfileImageUrl());
        leader.setRole(MemberRole.LEADER);
        leader.setUser(user);
        leader.setClub(club);

        club.addMember(leader);
        clubMemberRepository.save(leader);

        return club;
    }

    public ClubDetailDto getClubDetail(Long clubId) {
        Club club = findById(clubId);

        // 멤버 리스트에서 리더 찾기
        ClubMember leaderMember = club.getMembers().stream()
                .filter(m -> m.getRole() == MemberRole.LEADER)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_LEADER_NOT_FOUND));

        // LeaderDto 생성
        LeaderDto leaderDto = LeaderDto.builder()
                .memberId(leaderMember.getId())
                .memberName(leaderMember.getMemberName())
                .profileImgUrl(leaderMember.getProfileImgUrl())
                .build();

        // MemberDto 리스트 생성 (리더 제외)
        List<MemberDto> memberDtos = club.getMembers().stream()
                .filter(m -> m.getRole() != MemberRole.LEADER)
                .map(m -> MemberDto.builder()
                        .memberId(m.getId())
                        .name(m.getMemberName())
                        .profileImageUrl(m.getProfileImgUrl())
                        .build())
                .collect(Collectors.toList());

        // 최종 ClubDetailDto 생성
        return ClubDetailDto.builder()
                .id(club.getId())
                .clubName(club.getClubName())
                .clubDesc(club.getClubDesc())
                .category(club.getCategory().name()) // 문자열
                .campus(club.getCampus().name())
                .memberCount(club.getMembers().size())
                .leader(leaderDto)
                .members(memberDtos)
                .build();
    }

    @Transactional
    public void updateClub(Long clubId, ClubUpdateRequestDto clubDto ) {
        Club club = findById(clubId);
        if( clubDto.getClubName() != null ) club.setClubName( clubDto.getClubName() );
        if( clubDto.getClubDesc() !=null ) club.setClubDesc(clubDto.getClubDesc() );
        if( clubDto.getCampus() != null ) club.setCampus(Campus.fromString(clubDto.getCampus()));

        log.info("club:{} - 동아리가 정상적으로 수정되었습니다.", clubId);
    }

    @Transactional
    public void deleteClub(Long clubId) {
        Club club = findById(clubId);
        clubRepository.delete(club);
        log.info("club:{} - 동아리가 정상적으로 삭제되었습니다.", clubId);
    }
}
