package com.cumulus.backend.club.domain;

public enum Campus {
    JUKJEON, CHEONAN;

    public static Campus fromString(String value) {
        return Campus.valueOf(value.toUpperCase());
    }
}
