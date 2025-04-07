package com.itda.moamoa.domain.spec.controller;

import com.itda.moamoa.domain.spec.dto.response.UserProfileDTO;
import com.itda.moamoa.domain.spec.service.ProfileService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getProfile(@PathVariable Long userId) {
        UserProfileDTO profileDTO = profileService.getProfileById(userId);
        
        ApiResponse<UserProfileDTO> response = ApiResponse.success(
                SuccessCode.OK,
                "프로필 조회에 성공했습니다.",
                profileDTO
        );
        
        return ResponseEntity.ok(response);
    }
} 