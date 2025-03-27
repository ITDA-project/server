package com.itda.moamoa.domain.form.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter             // Getter 자동 생성
@Setter             // Setter 자동 생성
@NoArgsConstructor  // 기본 생성자 생성
@AllArgsConstructor // 모든 필드 생성자 생성
public class FormRequestDTO {
    private String content;
}