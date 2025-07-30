package com.itda.moamoa.global.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    private Long receiverId;
    private String title;
    private String body;
    private NotificationType notificationType;

    // 페이지 이동 관련 정보
    private Long postId;        // 신청서 거절
    private Long roomId;        // 신청서 수락
}