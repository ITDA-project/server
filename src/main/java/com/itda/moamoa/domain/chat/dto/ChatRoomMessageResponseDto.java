package com.itda.moamoa.domain.chat.dto;

import com.itda.moamoa.domain.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 채팅방 메시지 조회 응답 DTO
 */
@Getter
@Setter
@AllArgsConstructor
public class ChatRoomMessageResponseDto {
    private String sender; //이름
    private String content; //내용
    private LocalDateTime createdAt; //보낸 시간
    private Long userId; // 누가 보냈는지 식별

    public static ChatRoomMessageResponseDto fromMessage(ChatMessage message){
        return new ChatRoomMessageResponseDto(message.getSender().getName()
        , message.getContent()
        ,message.getCreatedAt()
        ,message.getSender().getId());
    }
}
