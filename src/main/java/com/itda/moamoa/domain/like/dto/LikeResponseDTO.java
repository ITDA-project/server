package com.itda.moamoa.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter                 // Getter 생성 Annotation
@NoArgsConstructor      // 기본 생성자 생성 Annotation
@AllArgsConstructor     // 일반 생성자 생성 Annotation
public class LikeResponseDTO {
    private long like_id;
    private long user_id;
    private long post_id;
}
