package com.itda.moamoa.domain.spec.service;

import com.itda.moamoa.domain.spec.dto.response.UserProfileDTO;
import com.itda.moamoa.domain.spec.entity.Spec;
import com.itda.moamoa.domain.spec.repository.SpecRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.common.ErrorCode;
import com.itda.moamoa.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final UserRepository userRepository;
    private final SpecRepository specRepository;

    @Transactional(readOnly = true)
    public UserProfileDTO getProfileById(Long userId) {
        // 사용자 ID로 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 사용자의 Spec 정보 조회
        Spec spec = specRepository.findByUser(user).orElse(null);
        
        return UserProfileDTO.builder()
                .name(user.getName())  // 사용자 이름
                .image(user.getImage())  // 프로필 이미지 URL
                .ratingAverage(user.getRatingAverage()) // 평균 별점
                .career(spec != null ? spec.getCareer() : null)  // 경력 정보
                .build();
    }
} 