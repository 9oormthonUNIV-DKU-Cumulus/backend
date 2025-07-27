package com.cumulus.backend.club.service;

import com.cumulus.backend.club.domain.*;
import com.cumulus.backend.club.dto.ClubApplicationDetailDto;
import com.cumulus.backend.club.dto.ClubApplicationListByApplicant;
import com.cumulus.backend.club.dto.ClubApplicationListByLeader;
import com.cumulus.backend.club.repository.ClubApplicationRepository;
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
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ClubApplicationService {

    private final ClubApplicationRepository clubApplicationRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberService clubMemberService;

    public ClubApplication findById(Long applicationId){
        return clubApplicationRepository.findOne(applicationId)
                .orElseThrow(()-> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
    }

    @Transactional
    public void createClubApplication(Club club, User applicant){
        // 동아리원신청 제어
        clubMemberRepository.findByUserIdAndClubId(applicant.getId(), club.getId())
                .ifPresent(cm -> {
                    throw new CustomException(ErrorCode.ALREADY_CLUB_MEMBER);
                });

        // 중복신청제어
        boolean alreadyApplied = clubApplicationRepository
                .existsByClubAndApplicant(club, applicant);
        if (alreadyApplied) {
            throw new CustomException(ErrorCode.DUPLICATE_CLUB_APPLICATION);
        }

        ClubApplication clubApplication = ClubApplication.builder()
                .club(club)
                .user(applicant)
                .createdAt(LocalDateTime.now())
                .status(ApplyStatus.PENDING)
                .build();

        ClubApplication savedApplication = clubApplicationRepository.save(clubApplication);
        log.info("모임신청 등록완료 - 신청id{}", savedApplication.getId());
    }

    public ClubApplicationDetailDto getApplicationDetail(Long ApplicationId){
        ClubApplication clubApplication = findById(ApplicationId);
        return ClubApplicationDetailDto.builder()
                .clubApplicationId(clubApplication.getId())
                .clubName(clubApplication.getClub().getClubName())
                .clubDesc(clubApplication.getClub().getClubDesc())
                .campus(clubApplication.getClub().getCampus().name())
                .category(clubApplication.getClub().getCategory().name())
                .createdAt(clubApplication.getCreatedAt())
                .applicationStatus(clubApplication.getStatus().name())
                .build();
    }

    @Transactional
    public void deleteClubApplication(ClubApplication clubApplication, User user){
        if(!clubApplication.getUser().equals(user)) {
            log.error("동아리신청내역 삭제권한 없음");
            throw new CustomException(ErrorCode.NO_PERMISSION_CLUB_APPLICATION);
        }

        clubApplicationRepository.delete(clubApplication);
        log.info("동아리신청 삭제완료");
    }

    public List<ClubApplicationListByApplicant> getApplicationByApplicant(User user) {
        return clubApplicationRepository.findByApplicant(user).stream()
                .map(ClubApplicationListByApplicant::fromEntity)
                .toList();
    }

    public List<ClubApplicationListByLeader> getApplicationByLeader(User user) {
        // 리더로 있는 Club리스트 조회
        List<Club> leadingClubs = clubRepository.findClubsByLeaderUser(user);
        if (leadingClubs.isEmpty()) return Collections.emptyList();

        // 해당 클럽들의 신청서 목록조회
        List<ClubApplication> clubApplications = clubApplicationRepository.findByClubs(leadingClubs);
        return clubApplications.stream().map(ClubApplicationListByLeader::fromEntity).toList();
    }

    @Transactional
    public void updateApplicationApprove(Long clubApplicationId, Long userId) {
        ClubApplication clubApplication = findById(clubApplicationId);

        // 승인수행자가 해당 동아리의 리더인지 확인
        clubMemberService.checkClubLeaderOrThrow(userId, clubApplication.getClub().getId());
        // 승인상태가 PENDING인지 확인
        if (clubApplication.getStatus() != ApplyStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_APPLICATION_STATUS);
        }

        // 신청 승인상태로 변경
        clubApplication.setStatus(ApplyStatus.APPROVE);

        // 클럽멤버로 등록
        ClubMember clubMember = ClubMember.builder()
                .memberName(clubApplication.getUser().getUserName())
                .profileImgUrl(clubApplication.getUser().getProfileImageUrl())
                .role(MemberRole.MEMBER)
                .user(clubApplication.getUser())
                .club(clubApplication.getClub())
                .build();
        ClubMember savedClubMember = clubMemberRepository.save(clubMember);
        clubApplication.getClub().addMember(savedClubMember);
        log.info("동아리 신청 승인후 새로운 동아리 멤버 등록 - memberId:{}", savedClubMember.getId());
    }

    @Transactional
    public void updateApplicationReject(Long applicationId, Long userId) {
        ClubApplication clubApplication = findById(applicationId);

        // 승인수행자가 해당 동아리의 리더인지 확인
        clubMemberService.checkClubLeaderOrThrow(userId, clubApplication.getClub().getId());
        // 승인상태가 PENDING인지 확인
        if (clubApplication.getStatus() != ApplyStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_APPLICATION_STATUS);
        }

        // 승인 거부상태로 변경
        clubApplication.setStatus(ApplyStatus.REJECT);
        log.info("동아리 신청 거부처리 완료");
    }
}
