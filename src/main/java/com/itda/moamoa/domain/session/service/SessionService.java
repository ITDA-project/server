package com.itda.moamoa.domain.session.service;

import com.itda.moamoa.domain.session.dto.SessionRequestDTO;
import com.itda.moamoa.domain.session.dto.SessionResponseDTO;
import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.session.repository.SessionRepository;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.somoim.repository.SomoimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SomoimRepository somoimRepository;

    // 회차 생성
    @Transactional
    public SessionResponseDTO createSession(SessionRequestDTO request) {
        Somoim somoim = somoimRepository.findById(request.somoimId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소모임입니다."));
        
        Session session = Session.builder()
                .somoim(somoim)
                .sessionNumber(request.sessionNumber())
                .sessionDate(request.sessionDate())
                .price(request.price())
                .location(request.location())
                .description(request.description())
                .status(Session.SessionStatus.SCHEDULED)
                .build();
        
        sessionRepository.save(session);
        
        return SessionResponseDTO.from(session);
    }
    
    // 소모임별 회차 목록 조회
    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getSessions(Long somoimId) {
        Somoim somoim = somoimRepository.findById(somoimId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소모임입니다."));
                
        return sessionRepository.findBySomoimOrderBySessionNumberAsc(somoim)
                .stream()
                .map(SessionResponseDTO::from)
                .collect(Collectors.toList());
    }
    
    // 회차 상세 조회
    @Transactional(readOnly = true)
    public SessionResponseDTO getSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회차입니다."));
                
        return SessionResponseDTO.from(session);
    }
    
    // 회차 상태 변경
    @Transactional
    public SessionResponseDTO updateSessionStatus(Long sessionId, Session.SessionStatus status) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회차입니다."));
                
        session.updateStatus(status);
        sessionRepository.save(session);
        
        return SessionResponseDTO.from(session);
    }
} 