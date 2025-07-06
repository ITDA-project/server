package com.itda.moamoa.domain.session.dto;

import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.somoim.entity.SomoimStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SessionEndResponseDTO {
    private Long sessionId;
    private Long roomId;
    private Session.SessionStatus sessionStatus;
    private SomoimStatus somoimStatus;
    private String message;
    
    public SessionEndResponseDTO(Long sessionId, Long roomId, Session.SessionStatus sessionStatus, 
                                SomoimStatus somoimStatus, String message) {
        this.sessionId = sessionId;
        this.roomId = roomId;
        this.sessionStatus = sessionStatus;
        this.somoimStatus = somoimStatus;
        this.message = message;
    }
} 