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

import org.springframework.web.bind.annotation.*;



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




    
    // 채팅방 기반 현재 진행 중인 세션 조회
    @GetMapping("/chatroom/{roomId}/active")
    public ResponseEntity<ApiResponse<SessionResponseDTO>> getActiveSessionByChatRoom(@PathVariable Long roomId) {
        SessionResponseDTO response = sessionService.getActiveSessionByChatRoom(roomId);
        
        if (response == null) {
            // 진행 중인 세션이 없는 경우
            ApiResponse<SessionResponseDTO> apiResponse = ApiResponse.success(
                SuccessCode.OK,
                "진행 중인 세션이 없습니다.",
                null
            );
            return ResponseEntity.ok(apiResponse);
        }
        
        // 진행 중인 세션이 있는 경우
        ApiResponse<SessionResponseDTO> apiResponse = ApiResponse.success(
            SuccessCode.OK,
            "진행 중인 세션을 조회했습니다.",
            response
        );
        return ResponseEntity.ok(apiResponse);
    }
} 