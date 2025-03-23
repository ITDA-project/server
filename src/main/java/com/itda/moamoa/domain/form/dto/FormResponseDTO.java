package com.itda.moamoa.domain.form.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter             // Getter 자동 생성 Annotation
@NoArgsConstructor  // 기본 생성자 생성 Annotation
@AllArgsConstructor // 일반 생성자 생성 Annotation

public class FormResponseDTO {
    private long formId;
    private String content;
    private Long postId;
    private Long userId;
}
