package com.itda.moamoa.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 채팅방 초대 요청 DTO
 */
@Getter
@Setter
public class ChatRoomInviteRequestDto {
    private Long roomId; //초대할 roomId
    private String username; //초대할 상대방의 username
}
