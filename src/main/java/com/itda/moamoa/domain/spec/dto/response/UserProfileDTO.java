package com.itda.moamoa.domain.spec.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDTO {
    private String name;           // 사용자 이름
    private String image;        // 프로필 이미지 URL
    private Double ratingAverage;  // 평균 별점
    private String career;         // 경력 정보
} 