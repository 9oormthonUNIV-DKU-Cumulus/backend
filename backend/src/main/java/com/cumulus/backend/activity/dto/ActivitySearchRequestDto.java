package com.cumulus.backend.activity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivitySearchRequestDto {
    @NotNull(message = "모임글 목록의 카테고리는 반드시 넘겨줘야합니다.")
    @Min(value = 1, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요." +
            "1.스포츠  2.외국/언어  3.댄스  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
    @Max(value = 10, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요." +
            "1.스포츠  2.외국/언어  3.댄스  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
    private Integer categoryId;

    @Pattern(regexp = "latest|popular|all", message = "정렬 조건은 'latest', 'popular', 'all' 중 하나여야 합니다.")
    private String sort;
}
