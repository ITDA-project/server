package com.itda.moamoa.domain.session.dto;

import com.itda.moamoa.domain.session.entity.Session;
import java.time.LocalDate;
import java.time.LocalTime;

public record SessionResponseDTO(
    Long id,
    Long somoimId,
    int sessionNumber,
    LocalDate sessionDate,
    LocalTime sessionTime,
    int price,
    String status,
    int paymentCount
) {
    public static SessionResponseDTO from(Session session) {
        return new SessionResponseDTO(
            session.getId(),
            session.getSomoim().getId(),
            session.getSessionNumber(),
            session.getSessionDate(),
            session.getSessionTime(),
            session.getPrice(),
            session.getStatus().name(),
            session.getPayments().size()
        );
    }
} 