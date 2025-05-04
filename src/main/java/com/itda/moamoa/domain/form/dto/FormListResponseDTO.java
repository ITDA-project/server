package com.itda.moamoa.domain.form.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class FormListResponseDTO {
    
    private String postTitle;

    private List<FormItemDTO> forms;
}