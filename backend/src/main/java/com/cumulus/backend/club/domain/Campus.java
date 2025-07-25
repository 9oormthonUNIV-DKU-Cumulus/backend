package com.cumulus.backend.club.domain;

import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;

import java.util.Arrays;

public enum Campus {
    JUKJEON, CHEONAN;

    public static Campus fromString(String value) {
        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CAMPUS_NOT_FOUND));
    }
}
