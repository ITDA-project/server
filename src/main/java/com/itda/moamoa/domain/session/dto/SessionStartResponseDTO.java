package com.itda.moamoa.domain.session.dto;

import com.itda.moamoa.domain.session.entity.Session;
import java.time.LocalDate;
import java.time.LocalTime;

public record SessionStartResponseDTO(
    Long sessionId,        // 생성된 세션 ID
    Long roomId,          // 채팅방 ID
    int sessionNumber,    // 회차 번호
    LocalDate sessionDate, // 세션 날짜
    LocalTime sessionTime, // 세션 시간
    int price,            // 참가비
    String status         // 세션 상태
) {
    public static SessionStartResponseDTO from(Session session, Long roomId) {
        return new SessionStartResponseDTO(
            session.getId(),
            roomId,
            session.getSessionNumber(),
            session.getSessionDate(),
            session.getSessionTime(),
            session.getPrice(),
            session.getStatus().name()
        );
    }
} 