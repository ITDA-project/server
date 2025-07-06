package com.itda.moamoa.domain.session.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SessionRequestDTO(
    Long somoimId,
    int sessionNumber,
    LocalDate sessionDate,
    LocalTime sessionTime,
    int price
) {} 