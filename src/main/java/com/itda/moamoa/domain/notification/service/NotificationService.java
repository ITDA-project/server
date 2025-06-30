package com.itda.moamoa.domain.notification.service;
import com.itda.moamoa.domain.notification.dto.NotificationDto;
import com.itda.moamoa.domain.notification.entity.Notification;
import com.itda.moamoa.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.itda.moamoa.domain.notification.dto.NotificationType.FORM_SUBMITTED;

@Service //spring container(application context)로 configuration 등록용
@RequiredArgsConstructor //final, NonNull에 대해
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate; //WebSocket을 통해 실시간으로 메시지를 전송하는데 사용되는 템플릿
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    //알림 모두 조회
    public List<NotificationDto> getAllNotificationsForUser(Long receiverId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(receiverId);
        return notifications.stream() //stream으로 변환 - 각 요소에 함수 적용 가능
                .map(notification -> modelMapper.map(notification, NotificationDto.class)) //NotificationDto로 변환
                .collect(Collectors.toList()); //변환된 dto를 list로 수집
    }

    //알림 개별 조회

    //1. form 제출 알림 -> post 작성자가 받고 클릭 시 전체 form 목록으로 이동 -> 반환값이 notification에 대한 상세 정보 x
    public Optional<Notification> getNotificationByIdAndReceiverId(Long notificationId, Long receiverId) {

        //개별조회하고자 하는 Notification의 id + receiver id로 상세 개별 조회 => 폼 알림의 경우 필요 없음
        //notificationRepository.findByIdAndReceiverId(notificationId, receiverId);

        //해당 알림의 검증

        //이거는 근데 리다이렉트가 맞겠다
        //아니면 이미 있는 함수의 호출? formservice에서 ? formcontroller에 이미 있는데 그 역할을 notification controller에서도 하게 됨
        //애매한디

        //redirect 경로 + 알림 type dto로 반환해야

        if (notification.getType() == FORM_SUBMITTED):
            redirect "/forms"; //사용자 id에 대한 (postId에 대한)


    }

    //2. 결제 요청 알림 -> 해당 채팅방(소모임)의 해당 모임(회차)의 모달(금액, 참석, 거절)
    //somoimId의 meetId

    //알림 전송
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
