package com.itda.moamoa.domain.payment.repository;

import com.itda.moamoa.domain.payment.entity.Payment;
import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByImpUid(String impUid);
    Optional<Payment> findByMerchantUid(String merchantUid);
    
    // 사용자별 결제 내역 조회
    List<Payment> findByUserOrderByPaidAtDesc(User user);
    
    // 사용자와 소모임별 결제 내역 조회
    List<Payment> findByUserAndSomoimOrderByPaidAtDesc(User user, Somoim somoim);
    
    // 회차에 대한 결제 내역 조회
    List<Payment> findBySession(Session session);
    
    // 사용자가 특정 회차에 결제했는지 확인
    Optional<Payment> findByUserAndSessionAndStatus(User user, Session session, Payment.PaymentStatus status);
}
