package com.itda.moamoa.domain.payment.controller;

import com.itda.moamoa.domain.payment.dto.PaymentRefundRequest;
import com.itda.moamoa.domain.payment.dto.PaymentVerifyRequest;
import com.itda.moamoa.domain.payment.service.PaymentService;
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
}
