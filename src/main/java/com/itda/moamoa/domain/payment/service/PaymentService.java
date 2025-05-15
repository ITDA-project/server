package com.itda.moamoa.domain.payment.service;

import com.itda.moamoa.domain.payment.dto.PaymentRefundRequest;
import com.itda.moamoa.domain.payment.dto.PaymentVerifyRequest;
import com.itda.moamoa.domain.payment.entity.Payment;
import com.itda.moamoa.domain.payment.repository.PaymentRepository;
import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.session.repository.SessionRepository;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.somoim.repository.SomoimRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PortOneApiClient portOneApiClient;
    private final UserRepository userRepository;
    private final SomoimRepository somoimRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public void verifyPayment(PaymentVerifyRequest request, String userId) {
        // 아임포트에서 결제 내역 조회
        Map<String, Object> paymentInfo = portOneApiClient.getPaymentInfo(request.impUid());

        int amount = (int) paymentInfo.get("amount");
        String merchantUid = (String) paymentInfo.get("merchant_uid");
        String impUid = (String) paymentInfo.get("imp_uid");

        // DB 결제 정보 검증
        Optional<Payment> optionalPayment = paymentRepository.findByMerchantUid(merchantUid);
        
        // 사용자 조회 - 이메일 형식인 경우 username으로 조회
        User user;
        if (userId.contains("@")) {
            // 이메일 형식의 username으로 조회
            user = userRepository.findByUsername(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        } else {
            try {
                // 숫자 형식의 ID로 조회 시도
                Long userIdLong = Long.parseLong(userId);
                user = userRepository.findById(userIdLong)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
            } catch (NumberFormatException e) {
                // ID가 숫자 형식이 아닌 경우 username으로 조회
                user = userRepository.findByUsername(userId)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
            }
        }
        
        if (optionalPayment.isPresent()) {
            // 기존 결제 정보가 있는 경우
            Payment payment = optionalPayment.get();
            
            if (!payment.getUser().getId().toString().equals(user.getId().toString())) {
                throw new SecurityException("본인 결제가 아님");
            }

            if (payment.getAmount() != amount) {
                throw new IllegalArgumentException("결제 금액 불일치");
            }

            // 결제 상태 갱신
            payment.markPaid();
            paymentRepository.save(payment);
        } else {
            // 결제 정보가 없는 경우, 새로 생성
            Somoim somoim = null;
            Session session = null;
            
            if (request.somoimId() != null) {
                somoim = somoimRepository.findById(request.somoimId())
                        .orElse(null);
            }
            
            if (request.sessionId() != null) {
                session = sessionRepository.findById(request.sessionId())
                        .orElse(null);
            }
            
            // 새 결제 정보 생성
            Payment newPayment = Payment.builder()
                    .merchantUid(merchantUid)
                    .impUid(impUid)
                    .user(user)
                    .somoim(somoim)
                    .session(session)
                    .amount(amount)
                    .status(Payment.PaymentStatus.PAID)
                    .username(user.getUsername())
                    .build();
            
            newPayment.markPaid();
            paymentRepository.save(newPayment);
        }
    }

    @Transactional
    public void refundPayment(PaymentRefundRequest request, String userId) {
        Payment payment = paymentRepository.findByImpUid(request.impUid())
                .orElseThrow(() -> new IllegalArgumentException("결제 내역 없음"));

        User user;
        if (userId.contains("@")) {
            // 이메일 형식의 username으로 조회
            user = userRepository.findByUsername(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        } else {
            try {
                // 숫자 형식의 ID로 조회 시도
                Long userIdLong = Long.parseLong(userId);
                user = userRepository.findById(userIdLong)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
            } catch (NumberFormatException e) {
                // ID가 숫자 형식이 아닌 경우 username으로 조회
                user = userRepository.findByUsername(userId)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
            }
        }

        if (!payment.getUser().getId().toString().equals(user.getId().toString())) {
            throw new SecurityException("본인 결제만 환불 가능");
        }

        portOneApiClient.requestRefund(request.impUid(), request.amount());

        payment.markCancelled();
        paymentRepository.save(payment);
    }
}