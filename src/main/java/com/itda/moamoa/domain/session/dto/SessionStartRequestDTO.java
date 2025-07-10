package com.itda.moamoa.domain.session.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SessionStartRequestDTO(
    Long roomId,           // 채팅방 ID
    LocalDate sessionDate, // 세션 날짜
    LocalTime sessionTime, // 세션 시간
    String location,       // 모임 장소
    int price             // 참가비
) {
} 