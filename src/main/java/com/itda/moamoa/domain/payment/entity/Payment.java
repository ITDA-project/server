package com.itda.moamoa.domain.payment.entity;

import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String impUid;        // 아임포트 결제 고유 ID
    private String merchantUid;   // 상점 거래 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;            // 결제한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "somoim_id")
    private Somoim somoim;        // 소모임 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;      // 소모임 회차 정보

    private int amount;           // 결제 금액

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // 결제 상태 (PAID, CANCELLED, FAILED 등)

    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;

    // 쉽게 접근하기 위한 유저명 필드 (조회 편의성)
    private String username;

    public void markPaid() {
        this.status = PaymentStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    public void markCancelled() {
        this.status = PaymentStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public enum PaymentStatus {
        READY, PAID, CANCELLED, FAILED
    }
}
