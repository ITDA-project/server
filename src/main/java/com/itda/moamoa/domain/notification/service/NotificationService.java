package com.itda.moamoa.domain.notification.service;
import com.itda.moamoa.domain.notification.dto.NotificationDto;
import com.itda.moamoa.domain.notification.entity.Notification;
import com.itda.moamoa.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service //spring container(application context)로 configuration 등록용
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate; //WebSocket을 통해 실시간으로 메시지를 전송하는데 사용되는 템플릿
    private final NotificationRepository notificationRepository;

    //알림 모두 조회
    public List<NotificationDto> getAllNotificationsForUser(Long receiverId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(receiverId);
        return notifications.stream() //stream으로 변환 - 각 요소에 함수 적용 가능
                .map(NotificationDto::fromEntity) //NotificationDto로 변환
                .collect(Collectors.toList()); //변환된 dto를 list로 수집
    }

    //알림 개별 조회

    //1. form 제출 알림 -> post 작성자가 받고 클릭 시 전체 form 목록으로 이동 -> 반환값이 notification에 대한 상세 정보 x
    public Optional<Notification> getNotificationByIdAndReceiverId(Long notificationId, Long receiverId) {

        //개별조회하고자 하는 Notification의 id + receiver id로 상세 개별 조회 => 폼 알림의 경우 필요 없음
        //notificationRepository.findByIdAndReceiverId(notificationId, receiverId);

        //해당 알림의 검증

        //redirect 경로 + 알림 type dto로 반환해야


    }
    //2. 결제 요청 알림 -> 해당 채팅방(소모임)의 해당 모임(회차)의 모달(금액, 참석, 거절)
    //somoimId의 meetId

    public void notifyUser(String username, NotificationDto notificationDto) {
        messagingTemplate.convertAndSendToUser(username,
                "/queue/notifications",
                // /user/userA/queue/notifications으로 전송
                // setUserDestinationPrefix()로 /user/username이 추가됨

                // => WebSocket 세션과 username의 매핑
                notificationDto //NotificationDto로 Notification type, message, form id 전달
        );
    }
}
