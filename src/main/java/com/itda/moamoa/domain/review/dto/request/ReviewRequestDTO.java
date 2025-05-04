package com.itda.moamoa.domain.review.dto.request;

//import jakarta.validation.constraints.Max;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDTO {
    
    //@NotNull(message = "별점은 필수 입력 항목입니다.")
    //@Min(value = 0, message = "별점은 0점 이상이어야 합니다.")
    //@Max(value = 5, message = "별점은 5점 이하여야 합니다.")
    private Double star;
    
    //@NotBlank(message = "리뷰 내용은 필수 입력 항목입니다.")
    private String sentence;
    
    //@NotNull(message = "리뷰 대상 사용자 ID는 필수 입력 항목입니다.")
    private Long targetUserId;
} 