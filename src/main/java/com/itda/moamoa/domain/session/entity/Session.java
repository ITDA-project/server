package com.itda.moamoa.domain.session.entity;

import com.itda.moamoa.domain.payment.entity.Payment;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Session extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "somoim_id")
    private Somoim somoim;        // 소모임 정보

    private int sessionNumber;     // 회차 번호 (1회차, 2회차 등)

    private LocalDate sessionDate; // 회차 진행 예정일
    
    private LocalTime sessionTime; // 회차 진행 시간

    private String location;       // 모임 장소

    private int price;            // 회차별 참가 비용
    
    @Enumerated(EnumType.STRING)
    private SessionStatus status; // 회차 상태 (SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED)

    @Builder.Default
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>(); // 회차 참가 결제 정보

    // 회차 상태 변경 메서드
    public void updateStatus(SessionStatus status) {
        this.status = status;
    }

    public enum SessionStatus {
        SCHEDULED,     // 예정됨
        IN_PROGRESS,   // 진행 중
        COMPLETED,     // 완료됨
        CANCELLED      // 취소됨
    }
} 