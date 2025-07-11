package com.itda.moamoa.domain.form.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class FormResponseDTO {
    private String content;
    private String username; //고유한 필드
    private String userName;
    private String userImage;
}