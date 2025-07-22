package com.itda.moamoa.global.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListRequestDto {
    private Long receiverId;
    private String title;
    private String body;
    private NotificationType notificationType;

    private Long postId;
    private Long roomId;
}

