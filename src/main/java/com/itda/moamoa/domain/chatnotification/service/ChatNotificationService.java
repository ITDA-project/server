package com.itda.moamoa.domain.chatnotification.service;

import com.itda.moamoa.domain.chat.service.MessageService;
import com.itda.moamoa.domain.chatnotification.dto.CreateChatNotificationDto;
import com.itda.moamoa.domain.chatnotification.entity.ChatNotification;
import com.itda.moamoa.domain.chatnotification.repository.ChatNotificationRepository;
import com.itda.moamoa.domain.chatnotification.dto.ChatNotificationDto;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ChatNotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatNotificationRepository chatNotificationRepository;
    private final UserService userService;

    @Transactional
    public void notifyChatToUser(ChatNotificationDto chatNotificationDto) {

        for (String receiver : chatNotificationDto.getReceivers()) {
            messagingTemplate.convertAndSendToUser(
                    receiver, //dto의 각 username(전송 대상)

                    "/queue/chat-notifications",
                    // /user/userA/queue/chat-notifications으로 전송
                    // /prefix/username/broker/chat-notifications
                    // => WebSocket 세션과 username의 매핑
                    chatNotificationDto
            );
        }
    }


    //채팅 메시지에 대한 알림 저장
    @Transactional
    public List<ChatNotification> createChatNotification(CreateChatNotificationDto createChatNotificationDto) {
        User sender = userService.findUserByUsername(createChatNotificationDto.getSender())
                .orElseThrow(() -> new EntityNotFoundException("송신 유저 없음 "));

        List<ChatNotification> savedNotifications = new ArrayList<>();

        for (String receiverUsername : createChatNotificationDto.getReceivers()) {
            User receiver = userService.findUserByUsername(receiverUsername)
                    .orElseThrow(() -> new EntityNotFoundException("수신 유저 없음 "));

            ChatNotification chatNotification = ChatNotification.builder()
                    //dto의 field 중 entity에서는 다른 entity와의 매핑인 경우
                    //실제 entity에 담아주기 위해서는 실제 repository의 매핑된 entity를 가져와야함
                    .sender(sender)
                    .receiver(receiver)
                    .chatRoomId(createChatNotificationDto.getChatRoomId())
                    .chatMessageId(createChatNotificationDto.getChatMessageId())
                    //entity와 매핑되는 필드 아닌 경우 바로 dto의 값 적용
                    .chatNotificationType(createChatNotificationDto.getChatNotificationType())
                    .content(createChatNotificationDto.getMessage())
                    .createdAt(LocalDateTime.now())
                    .read(false)
                    .build();

            //전달받은 dto 바탕의 entity 생성 후 저장
            savedNotifications.add(chatNotificationRepository.save(chatNotification));


        }
        return savedNotifications;
    }

    public ChatNotificationDto notificationToDto(ChatNotification chatNotification, List<String> receivers){
        return ChatNotificationDto.builder()
        .id(chatNotification.getId())
        .createdAt(chatNotification.getCreatedAt())
        .chatNotificationType(chatNotification.getChatNotificationType())
        .message(chatNotification.getContent())
        .chatMessageId(chatNotification.getChatMessageId())
        .chatRoomId(chatNotification.getChatRoomId())
        .sender(chatNotification.getSender().getUsername())
        .receivers(receivers)
        .build();
    }

    //알림 검증 용도
    //채팅 메시지 알림 id로 가져오기
    public ChatNotification getChatNotification(Long id){
        return chatNotificationRepository.findChatNotificationById(id)
                .orElseThrow(() -> new EntityNotFoundException("알림이 존재하지 않습니다"));
    }

    //편의용 알림 생성 + 전송 메서드
    @Transactional
    public void createAndSendChatNotification(CreateChatNotificationDto createChatNotificationDto){
        //바로 알림 저장 후 전송이므로 유효성 검증 생략 가능
        List<ChatNotification> saved = createChatNotification(createChatNotificationDto);

        for(ChatNotification chatNotification : saved) {
            ChatNotificationDto chatNotificationDto = notificationToDto(
                    chatNotification,
                    List.of(chatNotification.getReceiver().getUsername())
            );
            //메서드 호출 자체로 Notification 생성 후 저장됨 - 변수의 선언 필요 x (사용할 필요 x이므로)
            notifyChatToUser(chatNotificationDto);
        }
    }
}
