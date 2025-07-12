package com.itda.moamoa.domain.notification.dto;

import com.itda.moamoa.global.fcm.dto.NotificationType;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String body;
    private NotificationType type;
    private String redirectUrl;
    private boolean isRead;
    private LocalDateTime createdAt;
}

