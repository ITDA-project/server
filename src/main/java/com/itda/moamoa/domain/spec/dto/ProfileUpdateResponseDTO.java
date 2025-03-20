package com.itda.moamoa.domain.spec.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class ProfileUpdateResponseDTO {
    private String career;
    private String imageUrl;
}