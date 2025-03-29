package com.itda.moamoa.domain.review.controller;

import com.itda.moamoa.domain.review.dto.request.ReviewRequestDTO;
import com.itda.moamoa.domain.review.dto.response.ReviewResponseDTO;
import com.itda.moamoa.domain.review.service.ReviewService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(
            @RequestBody ReviewRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("리뷰 생성 요청 - 대상 사용자 ID: {}", requestDTO.getTargetUserId());
        log.info("리뷰 생성 요청 - 별점: {}", requestDTO.getStar());
        log.info("리뷰 생성 요청 - 내용: {}", requestDTO.getSentence());
        log.info("리뷰 생성 요청 - 요청자: {}", userDetails.getUsername());
        
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