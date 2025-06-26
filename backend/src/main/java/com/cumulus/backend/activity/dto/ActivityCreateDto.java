package com.cumulus.backend.activity.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActivityCreateDto {

    @NotBlank(message = "모임글 제목은 반드시 입력해야합니다.")
    private String title;

    @NotNull(message = "모임글 카테고리는 반드시 입력해야합니다.")
    @Min(value = 1, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요. \n" +
            "1.스포츠  2.외국/언어  3.댄스  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
    @Max(value = 10, message = "카테고리는 1~10사이의 번호 값으로 지정해주세요. \n" +
            "1.스포츠  2.외국/언어  3.댄스  4.봉사활동  5.자기계발  6.독서/글  7.문화/공연  8.음악/악기  9.여행  10.업종/직무")
    private Integer categoryId;

    @NotBlank(message = "모임글 설명글 반드시 입력해야합니다.")
    private String description;

    @NotNull(message = "모임일자 반드시 입력해야합니다.")
    @Future(message = "모임일자는 반드시 미래여야 합니다.")
    private LocalDateTime meetingDate; // 모임일자

    @NotNull(message = "신청마감일자 반드시 입력해야합니다.")
    @Future(message = "신청 마감일자는 반드시 미래여야 합니다.")
    private LocalDateTime deadline; // 신청마감일자

    @NotNull(message = "최대인원수 반드시 입력해야합니다.")
    @Min(value = 1, message = "모집인원수는 최대 한명이상이 되어야합니다.")
    private Integer maxParticipants;

    private Long clubId;
}
