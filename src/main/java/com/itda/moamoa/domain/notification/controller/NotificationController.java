package com.itda.moamoa.domain.notification.controller;

import com.itda.moamoa.domain.notification.dto.NotificationResponseDto;
import com.itda.moamoa.domain.notification.service.NotificationService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // 커서 기반 알림 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getAllNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        String username = userDetails.getUsername();

        List<NotificationResponseDto> notifications = notificationService.getNotificationByCursor(cursor, size, username);

        ApiResponse<List<NotificationResponseDto>> response = ApiResponse.success(
                SuccessCode.OK,
                "알림 목록이 정상적으로 조회되었습니다.",
                notifications);

        return  ResponseEntity.ok(response);
    }

    // 알림 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> readNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        notificationService.readNotification(notificationId, userDetails.getUsername());

        ApiResponse<Void> response = ApiResponse.success(
                SuccessCode.OK,
                "알림이 읽음 처리되었습니다.",
                null
        );

        return ResponseEntity.ok(response);
    }

    // 알림 전체 읽음 처리
    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> readAllNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        notificationService.readAllNotifications(userDetails.getUsername());

        ApiResponse<Void> response = ApiResponse.success(
                SuccessCode.OK,
                "모든 알림이 읽음 처리되었습니다.",
                null
        );
        return ResponseEntity.ok(response);
    }
}
