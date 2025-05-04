package com.itda.moamoa.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE, FEMALE;

    // JSON -> Enum 변환
    @JsonCreator
    public static Gender fromString(String gender) {
        switch (gender.toUpperCase()) {
            case "M":
            case "MALE":
                return MALE;
            case "F":
            case "FEMALE":
                return FEMALE;
            default:
                throw new IllegalArgumentException("지원하지 않는 형식입니다: " + gender);
        }
    }
}
