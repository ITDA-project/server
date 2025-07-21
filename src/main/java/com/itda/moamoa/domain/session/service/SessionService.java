package com.itda.moamoa.domain.session.service;

import com.itda.moamoa.domain.notification.service.NotificationService;
import com.itda.moamoa.domain.participant.entity.Role;
import com.itda.moamoa.domain.participant.repository.ParticipantRepository;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.session.dto.SessionRequestDTO;
import com.itda.moamoa.domain.session.dto.SessionResponseDTO;
import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.session.repository.SessionRepository;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.somoim.repository.SomoimRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.fcm.dto.NotificationRequestDTO;
import com.itda.moamoa.global.fcm.dto.NotificationType;
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
    private final NotificationService notificationService;
    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;

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

        // 알림 전송
        notifyParticipant(somoim);
        
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

    // 알림 전송
    private void notifyParticipant(Somoim somoim) {
        // 주최자 제외 소모임 참가자 목록
        List<User> users = participantRepository.findAllBySomoimAndRole(somoim, Role.PARTICIPANT);

        // 주최자
        User host = participantRepository.findBySomoimAndRole(somoim, Role.ORGANIZER);
        // 주최자가 소모임 생성 직전 게시한 게시글
        Post post = postRepository.findTopByUserAndCreatedAtBeforeOrderByCreatedAtDesc(host, somoim.getCreatedAt());

        if (somoim == null) return;

        if (host != null && post != null) {
            for (User user : users) {
                notificationService.saveAndSendNotification(
                        NotificationRequestDTO.builder()
                                .receiverId(user.getId())
                                .title(post.getTitle())
                                .body("새로운 결제 요청이 있어요! 참여하시겠어요?")
                                .notificationType(NotificationType.PAYMENT_REQUESTED)
                                .build()
                );
            }
        }
    }
} 