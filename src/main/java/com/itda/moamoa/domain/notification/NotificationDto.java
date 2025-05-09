package com.itda.moamoa.domain.notification;

public class NotificationDto {
    private NotificationType type; //알림 종류 구분 -> 클라이언트의 ui 등 로직 구별 가능
    private String message; //알림 시 사용자에게 전달할 메시지
    private Long targetId; //postId, formId 등
}
