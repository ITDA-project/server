package com.itda.moamoa.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewEligibilityResponseDto {
    private int participationCount;
    private String message;
    
    public static ReviewEligibilityResponseDto of(int count) {
        String message = count == 0 
            ? "리뷰 권한이 없습니다" 
            : count + "번 같이 참여한 멤버입니다";
        return new ReviewEligibilityResponseDto(count, message);
    }
} 