package com.itda.moamoa.domain.form.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter             // Getter 자동 생성
@NoArgsConstructor  // 기본 생성자 생성
@AllArgsConstructor // 모든 필드 생성자 생성

public class FormResponseDTO {
    private long form_id;
    private String content;
    private Long post;
    private Long user;
}
