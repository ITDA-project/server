package com.itda.moamoa.domain.session.service;

import com.itda.moamoa.domain.session.dto.SessionRequestDTO;
import com.itda.moamoa.domain.session.dto.SessionResponseDTO;
import com.itda.moamoa.domain.session.dto.SessionStartRequestDTO;
import com.itda.moamoa.domain.session.dto.SessionStartResponseDTO;
import com.itda.moamoa.domain.session.dto.SessionEndRequestDTO;
import com.itda.moamoa.domain.session.dto.SessionEndResponseDTO;
import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.session.repository.SessionRepository;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.somoim.entity.SomoimStatus;
import com.itda.moamoa.domain.somoim.repository.SomoimRepository;
import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chat.repository.ChatRoomRepository;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.participant.entity.Participant;
import com.itda.moamoa.domain.participant.entity.Role;
import com.itda.moamoa.domain.participant.repository.ParticipantRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    // 모임 시작 - 세션 생성
    @Transactional
    public SessionStartResponseDTO startSession(SessionStartRequestDTO request, String username) {
        // 1. 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(request.roomId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));
        
        // 2. 채팅방 → 게시글 조회
        Post post = postRepository.findAll().stream()
                .filter(p -> p.getChatRoom() != null && p.getChatRoom().getId().equals(request.roomId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("채팅방에 연결된 게시글이 없습니다."));
        
        // 3. 사용자 권한 확인 (게시글 작성자인지 확인)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        
        if (!post.getUser().equals(user)) {
            throw new IllegalStateException("모임을 시작할 권한이 없습니다. 게시글 작성자만 가능합니다.");
        }
        
        // 4. 게시글 → 소모임 조회
        Participant organizer = participantRepository.findByPostAndRole(post, Role.ORGANIZER)
                .orElseThrow(() -> new EntityNotFoundException("소모임 주최자 정보를 찾을 수 없습니다."));
        
        Somoim somoim = organizer.getSomoim();
        
        // 5. 소모임 상태가 BEFORE인지 확인
        if (somoim.getStatus() != SomoimStatus.BEFORE) {
            throw new IllegalStateException("이미 시작된 모임이거나 종료된 모임입니다.");
        }
        
        // 6. 다음 회차 번호 계산 (기존 세션 개수 + 1)
        List<Session> existingSessions = sessionRepository.findBySomoimOrderBySessionNumberAsc(somoim);
        int nextSessionNumber = existingSessions.size() + 1;
        
        // 7. 세션 생성
        Session session = Session.builder()
                .somoim(somoim)
                .sessionNumber(nextSessionNumber)
                .sessionDate(request.sessionDate())
                .sessionTime(request.sessionTime())
                .price(request.price())
                .status(Session.SessionStatus.IN_PROGRESS)
                .build();
        
        sessionRepository.save(session);
        
        // 8. 소모임 상태를 ACTIVE로 변경 (BEFORE → START)
        somoim.setSomoim(SomoimStatus.START);
        somoimRepository.save(somoim);
        
        // 9. 응답 생성
        return SessionStartResponseDTO.from(session, request.roomId());
    }

    // 모임 종료 - 세션 종료
    @Transactional
    public SessionEndResponseDTO endSession(SessionEndRequestDTO request, String username) {
        // 1. 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));
        
        // 2. 채팅방 → 게시글 조회
        Post post = postRepository.findAll().stream()
                .filter(p -> p.getChatRoom() != null && p.getChatRoom().getId().equals(request.getRoomId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("채팅방에 연결된 게시글이 없습니다."));
        
        // 3. 사용자 권한 확인 (게시글 작성자인지 확인)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        
        if (!post.getUser().equals(user)) {
            throw new IllegalStateException("모임을 종료할 권한이 없습니다. 게시글 작성자만 가능합니다.");
        }
        
        // 4. 세션 조회
        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 세션입니다."));
        
        // 5. 세션 상태 확인 (진행중인 세션인지)
        if (session.getStatus() != Session.SessionStatus.IN_PROGRESS) {
            throw new IllegalStateException("진행중인 세션이 아닙니다.");
        }
        
        // 6. 세션 상태를 COMPLETED로 변경
        session.updateStatus(Session.SessionStatus.COMPLETED);
        sessionRepository.save(session);
        
        // 7. 소모임 조회 및 상태 변경 (START → REVIEW)
        Participant organizer = participantRepository.findByPostAndRole(post, Role.ORGANIZER)
                .orElseThrow(() -> new EntityNotFoundException("소모임 주최자 정보를 찾을 수 없습니다."));
        
        Somoim somoim = organizer.getSomoim();
        
        // 소모임 상태가 START인지 확인
        if (somoim.getStatus() != SomoimStatus.START) {
            throw new IllegalStateException("시작된 상태의 모임이 아닙니다.");
        }
        
        // 소모임 상태를 REVIEW로 변경
        somoim.setSomoim(SomoimStatus.REVIEW);
        somoimRepository.save(somoim);
        
        // 8. 응답 생성
        return new SessionEndResponseDTO(
            session.getId(),
            request.getRoomId(),
            Session.SessionStatus.COMPLETED,
            SomoimStatus.REVIEW,
            "모임이 성공적으로 종료되었습니다."
        );
    }

    // 회차 생성
    @Transactional
    public SessionResponseDTO createSession(SessionRequestDTO request) {
        Somoim somoim = somoimRepository.findById(request.somoimId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소모임입니다."));
        
        Session session = Session.builder()
                .somoim(somoim)
                .sessionNumber(request.sessionNumber())
                .sessionDate(request.sessionDate())
                .sessionTime(request.sessionTime())
                .price(request.price())
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