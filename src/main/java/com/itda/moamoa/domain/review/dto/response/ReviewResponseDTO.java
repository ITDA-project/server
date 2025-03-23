package com.itda.moamoa.domain.review.dto.response;

import com.itda.moamoa.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponseDTO {
    private Double star;
    private String sentence;
    private LocalDateTime createdAt;
    private Long targetUserId;
    
    public static ReviewResponseDTO from(Review review) {
        return ReviewResponseDTO.builder()
                .star(review.getStar())
                .sentence(review.getSentence())
                .createdAt(review.getCreatedAt())
                .targetUserId(review.getTargetUser().getId())
                .build();
    }
} 