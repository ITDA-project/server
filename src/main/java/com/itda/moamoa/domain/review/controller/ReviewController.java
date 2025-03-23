package com.itda.moamoa.domain.review.controller;

import com.itda.moamoa.domain.review.dto.request.ReviewRequestDTO;
import com.itda.moamoa.domain.review.dto.response.ReviewResponseDTO;
import com.itda.moamoa.domain.review.service.ReviewService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(
            @RequestBody ReviewRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        ReviewResponseDTO responseDTO = reviewService.createReview(requestDTO, userDetails.getUsername());
        
        ApiResponse<ReviewResponseDTO> response = ApiResponse.success(
                SuccessCode.CREATED,
                "리뷰가 성공적으로 작성되었습니다.",
                responseDTO
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> getReviewsByUserId(
            @PathVariable Long userId) {
        
        List<ReviewResponseDTO> reviewList = reviewService.getReviewsByUserId(userId);
        
        ApiResponse<ReviewResponseDTO> response = ApiResponse.successList(
                SuccessCode.OK,
                "사용자의 리뷰 목록을 성공적으로 조회했습니다.",
                reviewList,
                reviewList.size()
        );
        
        return ResponseEntity.ok(response);
    }
} 