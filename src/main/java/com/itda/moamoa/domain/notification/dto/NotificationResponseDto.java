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
    private boolean isRaed;             // 알림 읽음 여부
    private LocalDateTime createdAt;
                                // 페이지 이동 관련 정보
    private Long postId;        // 신청서 거절
    private Long roomId;        // 신청서 수락
}

