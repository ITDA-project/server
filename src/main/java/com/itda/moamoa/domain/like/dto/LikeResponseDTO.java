package com.itda.moamoa.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter                 // Getter 생성
@NoArgsConstructor      // 기본 생성자 생성
@AllArgsConstructor     // 모든 필드 생성자 생성
public class LikeResponseDTO {
    private long like_id;
    private long user;
    private long post;
}
