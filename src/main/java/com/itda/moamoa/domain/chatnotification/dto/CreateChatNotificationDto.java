package com.itda.moamoa.domain.chatnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateChatNotificationDto {
    private String chatRoomName;
    private Long chatMessageId;
    private ChatNotificationType chatNotificationType;
    private String sender;
    private String content;
    private List<String> receivers;
}


