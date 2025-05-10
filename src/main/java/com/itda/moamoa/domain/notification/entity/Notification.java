package com.itda.moamoa.domain.notification.entity;

import com.itda.moamoa.domain.notification.dto.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)  // Entity Life Cycle Event을 Listen
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private long notificationId;

    @Enumerated(EnumType.STRING)
    private NotificationType type; //알림 종류 구분

    private String message; //알림 시 사용자에게 전달할 메시지

    private Long targetId; //postId, formId 등

    //생성날짜 - 생성된 순으로 알림 목록 조회용
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //receiver 필드 필요 - 받는 사람에 대한 db 저장용
    //Notification 하나는 한 receiver에게
    //receiver 한명은 Notification 여러개
    //1대다 : receiver 대 notification
    //@ManyToOne 으로 써줘야


}
