package com.cumulus.backend.club.domain;

import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Category {
    SPORTS(1, "스포츠"),
    LANGUAGE(2, "외국/언어"),
    PHOTO(3, "사진/영상"),
    VOLUNTEER(4, "봉사활동"),
    SELF_IMPROVEMENT(5, "자기계발"),
    READING_WRITING(6, "독서/글"),
    CULTURE_PERFORMANCE(7, "문화/공연"),
    MUSIC(8, "음악/악기"),
    TRAVEL(9, "여행"),
    JOB_CAREER(10, "업종/직무");

    private final int id;
    private final String label;

    public static Category fromId(int id){
        return Arrays.stream(values())
                .filter(c -> c.id == id)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
