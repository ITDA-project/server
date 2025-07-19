package com.itda.moamoa.domain.notification.service;

import com.itda.moamoa.domain.notification.dto.NotificationResponseDto;
import com.itda.moamoa.domain.notification.entity.Notification;
import com.itda.moamoa.domain.notification.repository.NotificationRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.common.ErrorCode;
import com.itda.moamoa.global.exception.CustomException;
import com.itda.moamoa.global.fcm.FcmService;
import com.itda.moamoa.global.fcm.dto.NotificationRequestDTO;
import com.itda.moamoa.global.fcm.dto.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    // 알림 저장 및 전송
    public void saveAndSendNotification(NotificationRequestDTO dto) {
        // 1. 사용자 찾기
        User user = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 2. 알림 저장
        Notification notification = Notification.builder()
                .user(user)
                .title(dto.getTitle())
                .body(dto.getBody())
                .type((NotificationType) dto.getNotificationType())
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        // 3. FCM 전송
        fcmService.sendNotification(dto);
    }

    // 커서 기반 알림 조회
    public List<NotificationResponseDto> getNotificationByCursor(Long cursor, int size, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없는 사용자입니다."));

        if (cursor == null || cursor <= 0) {
            cursor = Long.MAX_VALUE;
        }

        PageRequest pageRequest = PageRequest.of(0, size);

        List<Notification> notifications = notificationRepository.findByUserAndIdLessThanOrderByIdDesc(user, cursor, pageRequest);

        return notifications.stream()
                .map(notification -> NotificationResponseDto.builder()
                        .id(notification.getId())
                        .title(notification.getTitle())
                        .body(notification.getBody())
                        .type(notification.getType())
                        //.redirectUrl(notification.getRedirectUrl())
                        .read(notification.isRead())
                        .createdAt(notification.getCreatedAt())
                        .build())
                .toList();
    }

    public void readNotification(Long notificationId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Notification notification = notificationRepository.findByIdAndUser(notificationId, user)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.isRead()) {
            notification.Read();
            notificationRepository.save(notification);
        }
    }

    public void readAllNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Notification> notifications = notificationRepository.findByUserAndReadFalse(user);

        for (Notification notification : notifications) {
            notification.Read();
        }
        notificationRepository.saveAll(notifications);
    }
}

