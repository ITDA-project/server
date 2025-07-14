package com.itda.moamoa.global.config.websocket;

import com.itda.moamoa.domain.chat.interceptor.StompPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                       WebSocketHandler webSocketHandler,
                                      Map<String, Object> attributes){

        String username = (String)attributes.get("username");
        if (username == null) {
            System.out.println("username not found");
            throw new IllegalArgumentException("WebSocket handshake: username not found in attributes");
        }
        System.out.println("username found");
        return new StompPrincipal(username);
    }
}
