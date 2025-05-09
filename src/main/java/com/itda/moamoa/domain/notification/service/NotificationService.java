package com.itda.moamoa.domain.notification.service;

import com.itda.moamoa.domain.notification.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service //spring container(application context)로 configuration 등록용
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyUser(String username, NotificationDto notificationDto) {
        messagingTemplate.convertAndSendToUser(username,
                "/queue/notifications",
                notificationDto //NotificationDto로 Notification type, message, form id 전달
        );
    }
}
