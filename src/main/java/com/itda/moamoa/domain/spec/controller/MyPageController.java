package com.itda.moamoa.domain.spec.controller;

import com.itda.moamoa.domain.spec.dto.ProfileUpdateRequestDTO;
import com.itda.moamoa.domain.spec.dto.ProfileUpdateResponseDTO;
import com.itda.moamoa.domain.spec.service.MyPageService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import com.itda.moamoa.global.exception.CustomException;
import com.itda.moamoa.global.common.ErrorCode;
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

    @PostMapping(value = "/edit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ProfileUpdateResponseDTO>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(value = "career", required = false) ProfileUpdateRequestDTO requestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            Long userId = Long.parseLong(userDetails.getUsername()); // 또는 userDetails.getId() (사용자 구현에 따라 다름)
            ProfileUpdateResponseDTO responseDTO = myPageService.updateProfile(userId, requestDTO, image);

            ApiResponse<ProfileUpdateResponseDTO> response = ApiResponse.success(
                    SuccessCode.OK,
                    "프로필이 성공적으로 업데이트되었습니다.",
                    responseDTO
            );

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}