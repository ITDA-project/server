package com.itda.moamoa.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateResponseDTO {
    private Long postId;
    private Long roomId;
}
