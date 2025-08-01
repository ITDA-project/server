package com.itda.moamoa.domain.payment.controller;

import com.itda.moamoa.domain.payment.dto.PaymentRefundRequest;
import com.itda.moamoa.domain.payment.dto.PaymentStatusRequestDto;
import com.itda.moamoa.domain.payment.dto.PaymentStatusResponseDto;
import com.itda.moamoa.domain.payment.dto.PaymentVerifyRequest;
import com.itda.moamoa.domain.payment.dto.ReviewEligibilityRequestDto;
import com.itda.moamoa.domain.payment.dto.ReviewEligibilityResponseDto;
import com.itda.moamoa.domain.payment.service.PaymentService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody PaymentVerifyRequest request
    ) {
        paymentService.verifyPayment(request, user.getUsername());
        return ResponseEntity.ok("결제 검증 완료");
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refundPayment(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody PaymentRefundRequest request
    ) {
        paymentService.refundPayment(request, user.getUsername());
        return ResponseEntity.ok("환불 처리 완료");
    }

    @PostMapping("/status")
    public ResponseEntity<ApiResponse<PaymentStatusResponseDto>> getPaymentStatus(
            @RequestBody PaymentStatusRequestDto request
    ) {
        PaymentStatusResponseDto response = paymentService.getPaymentStatus(request.getRoomId(), request.getSessionId());
        
        ApiResponse<PaymentStatusResponseDto> apiResponse = ApiResponse.success(
            SuccessCode.OK,
            "결제 상태 조회 완료",
            response
        );
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 두 사용자가 함께 참여한 회차 개수를 조회하여 리뷰 작성 권한을 확인합니다.
     */
    @PostMapping("/review/eligibility")
    public ResponseEntity<ApiResponse<ReviewEligibilityResponseDto>> checkReviewEligibility(
            @RequestBody ReviewEligibilityRequestDto request
    ) {
        ReviewEligibilityResponseDto response = paymentService.checkReviewEligibility(request);
        
        ApiResponse<ReviewEligibilityResponseDto> apiResponse = ApiResponse.success(
            SuccessCode.OK,
            "리뷰 권한 확인 완료",
            response
        );
        return ResponseEntity.ok(apiResponse);
    }
}
