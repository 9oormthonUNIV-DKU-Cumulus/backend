package com.cumulus.backend.activity.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityUpdateDto {
    private String title;

    @Min(value = 1, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요." +
            "1.스포츠  2.외국/언어  3.댄스  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
    @Max(value = 10, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요." +
            "1.스포츠  2.외국/언어  3.댄스  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
    private Integer categoryId;

    private String description;

    @Future(message = "모임일자는 반드시 미래여야 합니다.")
    private LocalDateTime meetingDate; // 모임일자

    @Future(message = "신청 마감일자는 반드시 미래여야 합니다.")
    private LocalDateTime deadline; // 신청마감일자

    @Min(value = 1, message = "모집인원수는 최대 한명이상이 되어야합니다.")
    private Integer maxParticipants;
}
