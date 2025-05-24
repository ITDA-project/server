package com.itda.moamoa.domain.chatnotification.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatNotificationWebSocketHandler extends TextWebSocketHandler {
    public void afterConnectionEstablished(WebSocketSession session){
        String username = (String) session.getAttributes().get("username");
        System.out.println(username + "연결됨");
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message){
        //클라이언트로부터의 메시지 수신, 응답 시 필요

    }
}
