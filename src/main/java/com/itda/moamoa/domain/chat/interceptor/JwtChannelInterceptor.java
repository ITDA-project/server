package com.itda.moamoa.domain.chat.interceptor;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JWTUtil jwtUtil;

    public JwtChannelInterceptor(JWTUtil jwtTokenProvider) {
        this.jwtUtil = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
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

        return message;
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
}
