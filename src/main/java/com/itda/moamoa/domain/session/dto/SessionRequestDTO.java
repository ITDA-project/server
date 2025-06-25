package com.itda.moamoa.domain.session.dto;

import java.time.LocalDate;

public record SessionRequestDTO(
    Long somoimId,
    int sessionNumber,
    LocalDate sessionDate,
    int price,
    String location,
    String description
) {} 