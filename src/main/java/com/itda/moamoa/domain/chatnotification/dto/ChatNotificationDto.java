package com.itda.moamoa.domain.chatnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
//클라이언트에 전송, 저장된 ChatNotification dms dtodptj 조회 용도

public class ChatNotificationDto {
    private Long id;
    private LocalDateTime createdAt;
    private ChatNotificationType chatNotificationType; //CHAT, INVITE
    private String message; //실제 받은 메시지 - chat 알림 메시지 내용 자체는 클라이언트에서 가공 시 생략
    private Long chatMessageId; //해당 채팅 메시지
    private Long chatRoomId; //chatRoom으로 접근 용도
    private String sender;
    private List<String> receivers;
}
