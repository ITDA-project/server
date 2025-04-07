package com.itda.moamoa.domain.review.service;

import com.itda.moamoa.domain.participant.entity.Participant;
import com.itda.moamoa.domain.participant.entity.ParticipantStatus;
import com.itda.moamoa.domain.participant.repository.ParticipantRepository;
import com.itda.moamoa.domain.review.dto.request.ReviewRequestDTO;
import com.itda.moamoa.domain.review.dto.response.ReviewResponseDTO;
import com.itda.moamoa.domain.review.entity.Review;
import com.itda.moamoa.domain.review.repository.ReviewRepository;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.somoim.entity.SomoimStatus;
import com.itda.moamoa.domain.somoim.repository.SomoimRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.exception.CustomException;
import com.itda.moamoa.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final SomoimRepository somoimRepository;
    
    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO requestDTO, String username) {
        // 리뷰를 작성하는 사용자 조회
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 리뷰 대상 사용자 조회
        User targetUser = userRepository.findById(requestDTO.getTargetUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 자기 자신에 대한 리뷰 작성 방지
        if (currentUser.getId().equals(targetUser.getId())) {
            throw new CustomException(ErrorCode.CANNOT_REVIEW_SELF);
        }
        
//        // 현재 사용자가 참여한 소모임 중 REVIEW 상태인 소모임 찾기
//        List<Somoim> reviewSomoims = somoimRepository.findByStatusAndParticipantsUserId(
//                SomoimStatus.REVIEW,
//                currentUser.getId()
//        );
//
//        if (reviewSomoims.isEmpty()) {
//            throw new CustomException(ErrorCode.NO_REVIEW_SOMOIM);
//        }
//
//        // 대상 사용자가 동일한 소모임에 참여했는지 확인
//        boolean hasParticipatedTogether = participantRepository.existsByUserIdAndSomoimIn(
//                targetUser.getId(),
//                reviewSomoims
//        );
//
//        if (!hasParticipatedTogether) {
//            throw new CustomException(ErrorCode.NOT_PARTICIPATED_TOGETHER);
//        }
        
        // 리뷰 생성 및 저장 (작성자와 대상자 모두 저장)
        Review review = new Review(
                requestDTO.getStar(), 
                requestDTO.getSentence(), 
                targetUser,   // 리뷰 대상자
                currentUser   // 리뷰 작성자
        );
        
        Review savedReview = reviewRepository.save(review);
        
        // 대상 사용자의 평균 별점 업데이트
        targetUser.addReview(requestDTO.getStar());
        userRepository.save(targetUser);
        
        return ReviewResponseDTO.from(savedReview);
    }
    
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        List<Review> reviews = reviewRepository.findByTargetUser(user);
        
        return reviews.stream()
                .map(ReviewResponseDTO::from)
                .collect(Collectors.toList());
    }
} 