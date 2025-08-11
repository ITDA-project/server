package com.itda.moamoa.domain.payment.repository;

import com.itda.moamoa.domain.payment.entity.Payment;
import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    // 특정 세션의 여러 사용자 결제 내역 조회 (PAID 상태만)
    @Query("SELECT p FROM Payment p WHERE p.session.id = :sessionId AND p.user.id IN :userIds AND p.status = 'PAID'")
    List<Payment> findPaidPaymentsBySessionAndUsers(@Param("sessionId") Long sessionId, @Param("userIds") List<Long> userIds);
    
    // 두 사용자가 함께 참여한 회차 개수 조회 (리뷰 권한 확인용)
    @Query("SELECT COUNT(DISTINCT p1.session.id) " +
           "FROM Payment p1, Payment p2 " +
           "WHERE p1.user.id = :userId1 " +
           "AND p2.user.id = :userId2 " +
           "AND p1.session.id = p2.session.id " +
           "AND p1.somoim.id = p2.somoim.id " +
           "AND p1.status = 'CANCELLED' " +
           "AND p2.status = 'CANCELLED' " +
           "AND p1.session.status = 'COMPLETED'")
    Long countSharedParticipation(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
