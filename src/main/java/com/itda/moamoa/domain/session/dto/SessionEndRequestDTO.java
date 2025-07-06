package com.itda.moamoa.domain.session.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SessionEndRequestDTO {
    private Long roomId;
    private Long sessionId;
} 