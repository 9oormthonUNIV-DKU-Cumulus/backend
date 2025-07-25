package com.cumulus.backend.club.service;

import com.cumulus.backend.club.domain.ApplyStatus;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.domain.ClubApplication;
import com.cumulus.backend.club.repository.ClubApplicationRepository;
import com.cumulus.backend.club.repository.ClubMemberRepository;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ClubApplicationService {

    private final ClubApplicationRepository clubApplicationRepository;
    private final ClubMemberRepository clubMemberRepository;

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

    @Transactional
    public void deleteClubApplication(ClubApplication clubApplication, User user){
        if(!clubApplication.getUser().equals(user)) {
            log.error("동아리신청내역 삭제권한 없음");
            throw new CustomException(ErrorCode.NO_PERMISSION_APPLICATION);
        }

        clubApplicationRepository.delete(clubApplication);
        log.info("동아리신청 삭제완료");
    }
}
