package com.itda.moamoa.domain.notification.service;

import com.itda.moamoa.domain.notification.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service //spring container(application context)로 configuration 등록용
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate; //WebSocket을 통해 실시간으로 메시지를 전송하는데 사용되는 템플릿

    public void notifyUser(String username, NotificationDto notificationDto) {
        messagingTemplate.convertAndSendToUser(username,
                "/queue/notifications",
                // /user/userA/queue/notification으로 전송
                // setUserDestinationPrefix()로 /user/userId가 추가됨

                // => WebSocket 세션과 username의 매핑
                notificationDto //NotificationDto로 Notification type, message, form id 전달
        );
    }
}
