package com.itda.moamoa.domain.spec.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileDTO {
    private String name;     // 사용자 이름
    private String profile;  // 프로필 이미지 URL
    private String career;   // 경력 정보
}
