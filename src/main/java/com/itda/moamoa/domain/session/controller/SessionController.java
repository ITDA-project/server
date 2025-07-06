package com.itda.moamoa.domain.session.controller;

import com.itda.moamoa.domain.session.dto.SessionRequestDTO;
import com.itda.moamoa.domain.session.dto.SessionResponseDTO;
import com.itda.moamoa.domain.session.dto.SessionStartRequestDTO;
import com.itda.moamoa.domain.session.dto.SessionStartResponseDTO;
import com.itda.moamoa.domain.session.dto.SessionEndRequestDTO;
import com.itda.moamoa.domain.session.dto.SessionEndResponseDTO;
import com.itda.moamoa.domain.session.entity.Session;
import com.itda.moamoa.domain.session.service.SessionService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    // 모임 시작 - 세션 생성
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<SessionStartResponseDTO>> startSession(
            @RequestBody SessionStartRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails user) {
        
        SessionStartResponseDTO response = sessionService.startSession(request, user.getUsername());
        
        ApiResponse<SessionStartResponseDTO> apiResponse = ApiResponse.success(
            SuccessCode.CREATED, 
            "모임이 시작되었습니다.", 
            response
        );
        return ResponseEntity.ok(apiResponse);
    }

    // 모임 종료 - 세션 종료
    @PostMapping("/end")
    public ResponseEntity<ApiResponse<SessionEndResponseDTO>> endSession(
            @RequestBody SessionEndRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails user) {
        
        SessionEndResponseDTO response = sessionService.endSession(request, user.getUsername());
        
        ApiResponse<SessionEndResponseDTO> apiResponse = ApiResponse.success(
            SuccessCode.OK, 
            "모임이 종료되었습니다.", 
            response
        );
        return ResponseEntity.ok(apiResponse);
    }

    // 소모임별 회차 목록 조회
    @GetMapping("/somoim/{somoimId}")
    public ResponseEntity<List<SessionResponseDTO>> getSessions(@PathVariable Long somoimId) {
        List<SessionResponseDTO> sessions = sessionService.getSessions(somoimId);
        return ResponseEntity.ok(sessions);
    }

    // 회차 상세 조회
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponseDTO> getSession(@PathVariable Long sessionId) {
        SessionResponseDTO session = sessionService.getSession(sessionId);
        return ResponseEntity.ok(session);
    }

    // 회차 상태 변경
    @PatchMapping("/{sessionId}/status")
    public ResponseEntity<SessionResponseDTO> updateSessionStatus(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long sessionId,
            @RequestParam String status) {
        
        // TODO: 소모임 관리자 권한 확인 로직 추가 필요
        
        Session.SessionStatus sessionStatus = Session.SessionStatus.valueOf(status);
        SessionResponseDTO response = sessionService.updateSessionStatus(sessionId, sessionStatus);
        return ResponseEntity.ok(response);
    }
} 