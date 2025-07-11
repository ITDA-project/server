package com.itda.moamoa.global.config;

import com.itda.moamoa.domain.chat.interceptor.JwtChannelInterceptor;
import com.itda.moamoa.global.config.websocket.CustomHandshakeHandler;
import com.itda.moamoa.global.security.jwt.interceptor.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;
    private final CustomHandshakeHandler customHandshakeHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //postman 용도
        registry.addEndpoint("/ws")
                        .setAllowedOriginPatterns("*");

        registry.addEndpoint("/ws") //처음 서버와 WebSocket 연결 시 요청하는 곳
                .setAllowedOriginPatterns("*") //운영할 땐 *가 아닌 주소로 수정
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(customHandshakeHandler)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app") //@MessageMapping 과 매칭될 prefix 설정
                .enableSimpleBroker("/topic","/queue", "/user"); //해당 prefix로 시작하는 경로를 구독한 사용자에게 메시지 전달
        registry.setUserDestinationPrefix("/user");
    }
}
