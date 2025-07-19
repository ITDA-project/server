package com.itda.moamoa.domain.notification.dto;

import com.itda.moamoa.global.fcm.dto.NotificationType;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {
    private Long id;                    // 수신자
    private String title;
    private String body;
    private NotificationType type;
    // private String redirectUrl;         // 사용자 이동 페이지
    private boolean read;                  // 알림 읽음 여부
    private LocalDateTime createdAt;
}

