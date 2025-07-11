package com.itda.moamoa.domain.spec.service;

import com.itda.moamoa.domain.form.entity.Form;
import com.itda.moamoa.domain.form.repository.FormRepository;
import com.itda.moamoa.domain.like.entity.Like;
import com.itda.moamoa.domain.like.repository.LikeRepository;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.spec.dto.response.MyPageDTO;
import com.itda.moamoa.domain.spec.dto.response.UserProfileDTO;
import com.itda.moamoa.domain.spec.dto.request.ProfileUpdateRequestDTO;
import com.itda.moamoa.domain.spec.dto.response.ProfileUpdateResponseDTO;
import com.itda.moamoa.domain.spec.entity.Spec;
import com.itda.moamoa.domain.spec.repository.SpecRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.exception.CustomException;
import com.itda.moamoa.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    private final UserRepository userRepository;
    private final SpecRepository specRepository;
    private final PostRepository postRepository;
    private final FormRepository formRepository;
    private final LikeRepository likeRepository;
    private final S3Service s3Service;
    private final ModelMapper modelMapper;

    @Transactional
    public ProfileUpdateResponseDTO updateProfile(
            String username, ProfileUpdateRequestDTO requestDTO, MultipartFile imageFile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 경력 정보 업데이트
        if (requestDTO != null) {
            // 기존 Spec이 있는지 확인
            Spec spec = specRepository.findByUser(user)
                    .map(existingSpec -> existingSpec.updateCareer(requestDTO.getCareer()))
                    .orElseGet(() -> Spec.create(user, requestDTO.getCareer() != null ? requestDTO.getCareer() : ""));
            
            specRepository.save(spec);
        }

        // 이미지 업로드 및 저장
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // S3에 이미지 업로드하고 URL 받아오기
                String imageUrl = s3Service.uploadFile(imageFile);

                // User 엔티티의 image 필드 업데이트 (직접 메서드 호출)
                user.updateImage(imageUrl);
                userRepository.save(user);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        // 응답 DTO 생성
        Spec updatedSpec = specRepository.findByUser(user).orElse(null);

        return ProfileUpdateResponseDTO.builder()
                .career(updatedSpec != null ? updatedSpec.getCareer() : null)
                .imageUrl(user.getImage())
                .build();      
    }

    @Transactional(readOnly = true)
    public UserProfileDTO getProfile(String username) {
        // 사용자명으로 사용자 조회
        User user = userRepository.findByUsername(username)
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
    
    @Transactional(readOnly = true)
    public MyPageDTO getFullMyPage(String username) {
        // 사용자명으로 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 사용자의 Spec 정보 조회
        Spec spec = specRepository.findByUser(user).orElse(null);
        
        // 1. 신청한 모임 목록 조회 (사용자가 form을 등록한 post)
        List<Form> forms = formRepository.findByUser(user);
        List<MyPageDTO.PostDTO> joinedPosts = forms.stream()
                .map(form -> {
                    Post post = form.getPost();
                    // 삭제된 게시글은 제외
                    if (post != null && !post.isDeleteFlag()) {
                        return MyPageDTO.PostDTO.builder()
                                .id(post.getPostId())
                                .title(post.getTitle())
                                .createdAt(post.getCreatedAt())
                                .build();
                    }
                    return null;
                })
                .filter(dto -> dto != null) // null 제거
                .collect(Collectors.toList());
        
        // 2. 좋아요한 모임 목록 조회
        List<Like> likes = likeRepository.findByUser(user);
        List<MyPageDTO.PostDTO> likedPosts = likes.stream()
                .map(like -> {
                    Post post = like.getPost();
                    // 삭제된 게시글은 제외
                    if (post != null && !post.isDeleteFlag()) {
                        return MyPageDTO.PostDTO.builder()
                                .id(post.getPostId())
                                .title(post.getTitle())
                                .createdAt(post.getCreatedAt())
                                .userId(post.getUser().getId())
                                .build();
                    }
                    return null;
                })
                .filter(dto -> dto != null) // null 제거
                .collect(Collectors.toList());
        
        // 3. 내가 만든 모임 목록 조회
        List<Post> createdPosts = postRepository.findByUserAndDeleteFlagFalse(user);
        List<MyPageDTO.PostDTO> myCreatedPosts = createdPosts.stream()
                .map(post -> MyPageDTO.PostDTO.builder()
                        .id(post.getPostId())
                        .title(post.getTitle())
                        .createdAt(post.getCreatedAt())
                        .userId(post.getUser().getId())
                        .build())
                .collect(Collectors.toList());
        
        return MyPageDTO.builder()
                .name(user.getName())
                .image(user.getImage())
                .ratingAverage(user.getRatingAverage())
                .joinedPosts(joinedPosts)
                .likedPosts(likedPosts)
                .createdPosts(myCreatedPosts)
                .build();
    }
}