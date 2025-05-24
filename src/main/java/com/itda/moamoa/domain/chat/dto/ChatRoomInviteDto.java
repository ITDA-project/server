package com.itda.moamoa.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomInviteDto {
    private Long roomId; //초대할 roomId
    private String username; //초대할 상대방의 username
}
