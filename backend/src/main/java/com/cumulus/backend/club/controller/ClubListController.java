package com.cumulus.backend.club.controller;

import com.cumulus.backend.club.domain.Campus;
import com.cumulus.backend.club.domain.Category;
import com.cumulus.backend.club.dto.ClubSummaryDto;
import com.cumulus.backend.club.service.ClubService;
import com.cumulus.backend.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="동아리")
public class ClubListController {

    private final ClubService clubService;

    //api/clubs?category={id}&campus={Jukjeon/Cheonan}&sort={latest/popular/all}
    @GetMapping("/api/clubs")
    @Operation(summary = "동아리 목록조회", description = "카테고리 & 캠퍼스 & 정렬옵션에 따른 동아리 목록조회")
    public ResponseEntity<ApiResponse<?>> getClubList(

            @Parameter(description = "카테고리 ID (1~10)", required = true)
            @RequestParam(required = true)
            @Min(value = 1, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요." +
                    "1.스포츠  2.외국/언어  3.사진/영상  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
            @Max(value = 10, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요." +
                    "1.스포츠  2.외국/언어  3.사진/영상  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
            Integer categoryId,

            @Parameter(description = "캠퍼스 선택(JUKJEON/CHEONAN)", required = false)
            @RequestParam(required = false, defaultValue = "JUKJEON")
            @Pattern(regexp = "JUKJEON|CHEONAN",
                    message = "캠퍼스 선택은 JUKJEON, CHEONAN 중 하나여야 합니다.")
            String campusVal,

            @Parameter(description = "정렬기준: latest, popular, all", required = false)
            @RequestParam(required = false, defaultValue = "latest")
            @Pattern(regexp = "latest|popular|all",
                    message = "정렬 조건은 latest, popular, all 중 하나여야 합니다.")
            String sort
    ){
        Category category = Category.fromId(categoryId);
        Campus campus = Campus.fromString(campusVal);
        log.info("동아리 목록 조회 | 필터링조건 - category: {}, campus: {}, sort: {}", category.getLabel(), campus.name(), sort);
        List<ClubSummaryDto> clubSummaryDtoList = clubService.getClubListWithFilters(category, campus, sort);
        return ResponseEntity.ok(ApiResponse.success(clubSummaryDtoList));
    }
}
