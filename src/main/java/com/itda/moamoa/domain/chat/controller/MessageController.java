package com.itda.moamoa.domain.chat.controller;

import com.itda.moamoa.domain.chat.dto.MessageRequestDto;
import com.itda.moamoa.domain.chat.dto.MessageResponseDto;
import com.itda.moamoa.domain.chat.service.MessageService;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    //응답:메시지 내용, 메시지 보낸 사람, 전송 시간
    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, @AuthenticationPrincipal CustomUserDetails user, MessageRequestDto messageRequestDto){
        messageRequestDto.setUsername(user.getUsername());
        messageRequestDto.setRoomId(roomId);
        //service -> 보낸 메시지 내용을 저장
        MessageResponseDto messageResponseDto = messageService.saveMessage(messageRequestDto);
        //특정 채팅방에 메시지 DTO 를 전달
        messagingTemplate.convertAndSend("/topic/room/"+roomId,messageResponseDto);
    }
}
