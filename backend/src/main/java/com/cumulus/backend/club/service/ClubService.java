package com.cumulus.backend.club.service;

import com.cumulus.backend.activity.dto.ActivityDetailDto;
import com.cumulus.backend.club.domain.*;
import com.cumulus.backend.club.dto.ClubCreateRequestDto;
import com.cumulus.backend.club.dto.ClubSummaryDto;
import com.cumulus.backend.club.repository.ClubMemberRepository;
import com.cumulus.backend.club.repository.ClubRepository;
import com.cumulus.backend.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

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
}
