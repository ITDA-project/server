package com.itda.moamoa.global.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.itda.moamoa.domain.user.service.UserService;
import com.itda.moamoa.global.fcm.dto.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {
    private final UserService userService;

    public void sendNotification(NotificationRequestDTO dto) {
        String fcmToken = userService.getFcmTokenById(dto.getReceiverId());

        if (fcmToken == null || fcmToken.isBlank()) {
            log.warn("FCM 토큰 없음: 알림 전송 중단 (userId={})", dto.getReceiverId());
            return;
        }

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(dto.getTitle())
                        .setBody(dto.getBody())
                        .build())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
            log.info("알림 전송 성공 to userId={} ({})", dto.getReceiverId(), dto.getNotificationType());
        } catch (FirebaseMessagingException e) {
            log.error("알림 전송 실패", e);
        }
    }
}

