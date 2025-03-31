package com.itda.moamoa.domain.spec.controller;

import com.itda.moamoa.domain.spec.dto.response.MyPageDTO;
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
    
    @GetMapping("/full")
    public ResponseEntity<ApiResponse<MyPageDTO>> getFullMyPage(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        String username = userDetails.getUsername();
        MyPageDTO myPageDTO = myPageService.getFullMyPage(username);
        
        ApiResponse<MyPageDTO> response = ApiResponse.success(
                SuccessCode.OK,
                "마이페이지 전체 정보 조회에 성공했습니다.",
                myPageDTO
        );
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/edit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ProfileUpdateResponseDTO>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(value = "career", required = false) String career,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        String username = userDetails.getUsername();
        
        // String career를 ProfileUpdateRequestDTO로 변환
        ProfileUpdateRequestDTO requestDTO = null;
        if (career != null && !career.isEmpty()) {
            requestDTO = new ProfileUpdateRequestDTO();
            requestDTO.setCareer(career);
        }
        
        ProfileUpdateResponseDTO responseDTO = myPageService.updateProfile(username, requestDTO, image);

        ApiResponse<ProfileUpdateResponseDTO> response = ApiResponse.success(
                SuccessCode.OK,
                "프로필이 성공적으로 업데이트되었습니다.",
                responseDTO
        );

        return ResponseEntity.ok(response);
    }
}
