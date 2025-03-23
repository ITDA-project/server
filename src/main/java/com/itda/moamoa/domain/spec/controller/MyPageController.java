package com.itda.moamoa.domain.spec.controller;

import com.itda.moamoa.domain.spec.dto.response.ProfileDTO;
import com.itda.moamoa.domain.spec.dto.request.ProfileUpdateRequestDTO;
import com.itda.moamoa.domain.spec.dto.response.ProfileUpdateResponseDTO;
import com.itda.moamoa.domain.spec.service.MyPageService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        String username = userDetails.getUsername();
        ProfileDTO profileDTO = myPageService.getProfile(username);
        
        ApiResponse<ProfileDTO> response = ApiResponse.success(
                SuccessCode.OK,
                "프로필 조회에 성공했습니다.",
                profileDTO
        );
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/edit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ProfileUpdateResponseDTO>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(value = "career", required = false) ProfileUpdateRequestDTO requestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        String username = userDetails.getUsername();
        ProfileUpdateResponseDTO responseDTO = myPageService.updateProfile(username, requestDTO, image);

        ApiResponse<ProfileUpdateResponseDTO> response = ApiResponse.success(
                SuccessCode.OK,
                "프로필이 성공적으로 업데이트되었습니다.",
                responseDTO
        );

        return ResponseEntity.ok(response);
    }
}
