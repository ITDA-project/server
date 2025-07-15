package com.itda.moamoa.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 채팅방 응답 DTO
 */
@Getter
@Setter
@AllArgsConstructor
public class ChatRoomDto {
    private String roomName;
    private boolean deleteFlag;
}
