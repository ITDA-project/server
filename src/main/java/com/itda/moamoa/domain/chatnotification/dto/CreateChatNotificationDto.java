package com.itda.moamoa.domain.chatnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//ChatNotification 저장하기 위해 ChatService에서 정보를 받는 용도
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateChatNotificationDto {
    private ChatNotificationType chatNotificationType;
    private String message;
    private Long chatMessageId;
    private Long chatRoomId;
    private String sender;
    private List<String> receivers;
}
