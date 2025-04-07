package com.itda.moamoa.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponseDTO {
    private long like_id;
    private long user;
    private long post;
}
