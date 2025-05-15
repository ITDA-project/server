package com.itda.moamoa.domain.payment.service;

import com.itda.moamoa.domain.payment.dto.PaymentRefundRequest;
import com.itda.moamoa.domain.payment.dto.PaymentVerifyRequest;
import com.itda.moamoa.domain.payment.entity.Payment;
import com.itda.moamoa.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PortOneApiClient portOneApiClient;

    @Transactional
    public void verifyPayment(PaymentVerifyRequest request, String userId) {
        // 아임포트에서 결제 내역 조회
        Map<String, Object> paymentInfo = portOneApiClient.getPaymentInfo(request.impUid());

        int amount = (int) paymentInfo.get("amount");
        String merchantUid = (String) paymentInfo.get("merchant_uid");

        // DB 결제 정보 검증
        Payment payment = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 없음"));

        if (!payment.getUser().getId().toString().equals(userId)) {
            throw new SecurityException("본인 결제가 아님");
        }

        if (payment.getAmount() != amount) {
            throw new IllegalArgumentException("결제 금액 불일치");
        }

        // 결제 상태 갱신
        payment.markPaid();
        paymentRepository.save(payment);
    }

    @Transactional
    public void refundPayment(PaymentRefundRequest request, String userId) {
        Payment payment = paymentRepository.findByImpUid(request.impUid())
                .orElseThrow(() -> new IllegalArgumentException("결제 내역 없음"));

        if (!payment.getUser().getId().toString().equals(userId)) {
            throw new SecurityException("본인 결제만 환불 가능");
        }

        portOneApiClient.requestRefund(request.impUid(), request.amount());

        payment.markCancelled();
        paymentRepository.save(payment);
    }
}