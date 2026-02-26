package com.hospital.hms.common.enums;

public enum Gender {
    FEMALE("female"),
    MALE("male");
    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
