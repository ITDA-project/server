package com.itda.moamoa.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//WebSocket endpoint
//STOMP = simple text oriented messaging protocol - WebSocket 위에서 메시지 주고 받는 프로토콜

@Configuration
@EnableWebSocketMessageBroker //STOMP 기반 WebSocket 메시지 브로커
public class NotificationWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/queue");
        //client에게 server에서 broker을 거쳐 broadcasting
        //client가 메시지를 받을 경로 = 구독 경로

        config.setApplicationDestinationPrefixes("/app"); //client에서 server로 메시지를 보낼 때의 prefix

        config.setUserDestinationPrefix("/user"); //특정 사용자에게만 메시지 보내는 구조 활성화
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //결제 요청 알림 전용 엔드포인트
        registry.addEndpoint("/ws") //client의 WebSocket 연결 요청 ws://.../ws으로 연결
                .setAllowedOriginPatterns("*") //client 측에서 경로 결정되면 변경 (WebSocket cors)
                .withSockJS(); //WebSocket 지원 x 환경에서 대체 - 리액트네이티브앱은 WebSocket 지원
    }

}
