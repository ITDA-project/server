package com.itda.moamoa.domain.session.dto;

import com.itda.moamoa.domain.session.entity.Session;
import java.time.LocalDate;

public record SessionResponseDTO(
    Long id,
    Long somoimId,
    int sessionNumber,
    LocalDate sessionDate,
    int price,
    String status,
    String location,
    String description,
    int paymentCount
) {
    public static SessionResponseDTO from(Session session) {
        return new SessionResponseDTO(
            session.getId(),
            session.getSomoim().getId(),
            session.getSessionNumber(),
            session.getSessionDate(),
            session.getPrice(),
            session.getStatus().name(),
            session.getLocation(),
            session.getDescription(),
            session.getPayments().size()
        );
    }
} 