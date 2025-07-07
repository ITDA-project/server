package com.itda.moamoa.domain.session.repository;

import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    
    // 소모임 ID로 모든 회차 조회
    List<Session> findBySomoimOrderBySessionNumberAsc(Somoim somoim);
    
    // 소모임 ID와 회차 번호로 회차 조회
    Session findBySomoimAndSessionNumber(Somoim somoim, int sessionNumber);
    
    // 특정 소모임의 현재 진행 중인 세션 조회
    Optional<Session> findBySomoimAndStatus(Somoim somoim, Session.SessionStatus status);
} 