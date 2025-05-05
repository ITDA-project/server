package com.itda.moamoa.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDTO {
    private Long postId;        // 커서로 사용
    private String title;
    private Integer likesCount;
    private LocalDateTime createdAt;
    private Long userId;        // 작성자 ID
}
