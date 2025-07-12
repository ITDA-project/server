package com.itda.moamoa.domain.notification.entity;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.fcm.dto.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;                  // 수신인

    private String title;               // 알림 제목
    private String body;                // 알림 세부 내용

    private NotificationType type;      // 알림 유형 ex. 결제, 신청서 등
    private String redirectUrl;         // 클릭 시 이동 경로

    private boolean isRead = false;     // 알림 읽음 여부

    private LocalDateTime createdAt = LocalDateTime.now();

    public void Read() {          // 알림 읽음
        this.isRead = true;
    }
}
