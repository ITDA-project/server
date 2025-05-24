package com.itda.moamoa.global.security.jwt.interceptor;

import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import io.jsonwebtoken.impl.JwtTokenizer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;
//WebSocket 연결 시 HTTP처럼 Authorization 헤더 사용하지는 못함
//클라이언트는 쿼리 파라미터, 첫 메시지로 JWT 보냄
//1. WebSocket 연결 시 클라이언트가 보낸 토큰을 추출, 인증
//2. 인증된 사용자 정보를 세션에 저장, 메시지에 대한 권한 검사에 사용

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JWTUtil jwtUtil;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception{
        URI uri = request.getURI(); //websocket 요청의 uri 객체 가져옴
        String query = uri.getQuery(); //uri 중 쿼리 스트링 추출
        String token = null;

        if(query != null) {

            for(String param : query.split("&")){ //여러 쿼리 파라미터 가능 -> & 기준 분리
                String[] pair = param.split("="); //각 파라미터를 key=value 형태로 나눔
                if (pair.length == 2 && pair[0].equals("token")) { //key=="token"
                    token = pair[1];
                }
            }
        }
        if(token != null && ! (jwtUtil.isExpired(token))){ //토큰 유효 시
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            attributes.put("username", username); //WebSocketSession에 데이터 전달 - WebSocket 연결 성립된 후에도 유지
            attributes.put("role", role);
            return true;

        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception exception){
        //필요 시 로그 출력
    }
}
