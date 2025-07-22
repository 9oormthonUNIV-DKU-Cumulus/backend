package com.cumulus.backend.club.dto;

import com.cumulus.backend.club.domain.Category;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubCreateRequestDto {
    @NotNull(message = "동아리 이름은 반드시 입력해야합니다.")
    private String clubName;

    @NotBlank(message = "동아리 상세 설명글은 반드시 입력해야합니다.")
    private String clubDesc;

    @Min(value = 1, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요." +
            "1.스포츠  2.외국/언어  3.사진/영상  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
    @Max(value = 10, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요." +
            "1.스포츠  2.외국/언어  3.사진/영상  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
    private int category;

    @Pattern(regexp = "JUKJEON|CHEONAN",
            message = "캠퍼스 선택은 JUKJEON, CHEONAN 중 하나여야 합니다.")
    private String campus;
}
