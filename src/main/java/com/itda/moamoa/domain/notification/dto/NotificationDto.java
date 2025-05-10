package com.itda.moamoa.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private NotificationType type; //알림 종류 구분 -> 클라이언트의 ui 등 로직 구별 가능
    private String message; //알림 시 사용자에게 전달할 메시지
    private Long targetId; //formId
    private LocalDateTime createdAt; //알림 생성 시각
    // 실제 생명주기 관리 x(db 저장), 엔티티 데이터 전송 용도 => EntityListeners 생략
}
