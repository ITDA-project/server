package com.itda.moamoa.domain.chat.interceptor;

import com.itda.moamoa.domain.chat.repository.ChatRoomUserRepository;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@Component
@AllArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JWTUtil jwtUtil;
    private final ChatRoomUserRepository chatRoomUserRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("preSend");
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor == null){
            return message;
        }

        StompCommand command = accessor.getCommand();

        if(command == null) return message;

        switch(command) {
            case CONNECT:
                System.out.println("connect");
                authenticateUser(accessor);
                break;
            case SUBSCRIBE:
                //채팅방 접근 권한 확인
                String destination = accessor.getDestination();

                Principal user = accessor.getUser();
                if (user == null) {
                    throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
                }
                String currentUser = user.getName();

                if (destination != null && destination.startsWith("/topic/chat/room/")) {
                    try {
                        Long roomId = extractRoomId(destination);

                        if (!hasAccessToRoom(currentUser, roomId)) {
                            throw new IllegalArgumentException("채팅방 접근 권한이 존재하지 않습니다.");
                        }
                    } catch (NumberFormatException e) {
                        log.warn("잘못된 roomId 형식 : {}", destination);
                        throw new IllegalArgumentException("올바르지 않은 채팅방 경로입니다.");
                    }
                }

                break;
            default:
                break;
        }
        return message;
    }

    private void authenticateUser(StompHeaderAccessor accessor){
        // 클라이언트가 CONNECT 프레임을 보낼 때 헤더에서 JWT 추출
        String accessToken = accessor.getFirstNativeHeader("access");
        if (accessToken == null) {
            throw new IllegalArgumentException("access token이 존재하지 않습니다.");
        }

        if (!validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // 토큰에서 사용자 정보 추출
        String username = jwtUtil.getUsername(accessToken);

        // 인증 객체를 SecurityContext에 저장하거나,
        // accessor에 Principal 설정
        accessor.setUser(new StompPrincipal(username));
    }

    private boolean validateToken(String token){
        //토큰 만료 여부 확인
        try{
            jwtUtil.isExpired(token);
        } catch(ExpiredJwtException e){
            return false;
        }

        //access token 인지 category로 확인 - 아닐 시 응답, 다음 필터로 x
        String category = jwtUtil.getCategory(token);
        if(!category.equals("access")) {
            return false;
        }
        return true;
    }

    private Long extractRoomId(String destination){
        if(destination == null) return null;
        String[] parts = destination.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }

    private boolean hasAccessToRoom(String username,Long roomId){
        return chatRoomUserRepository.existsByUsernameAndRoomId(username,roomId);
    }
}
