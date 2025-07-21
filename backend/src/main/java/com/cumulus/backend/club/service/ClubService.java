package com.cumulus.backend.club.service;

import com.cumulus.backend.activity.dto.ActivityDetailDto;
import com.cumulus.backend.club.domain.Campus;
import com.cumulus.backend.club.domain.Category;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.dto.ClubSummaryDto;
import com.cumulus.backend.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private ClubRepository clubRepository;

    public List<ClubSummaryDto> getClubListWithFilters(Category category, Campus campus, String sort){
        List<Club> clubs = clubRepository.search(category, campus, sort);

        return clubs.stream()
                .map(ClubSummaryDto::fromEntity)
                .collect(Collectors.toList());
    }


}
