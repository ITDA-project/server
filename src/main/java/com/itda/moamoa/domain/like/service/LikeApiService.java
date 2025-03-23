package com.itda.moamoa.domain.like.service;

import com.itda.moamoa.domain.like.dto.LIkeRequestDTO;
import com.itda.moamoa.domain.like.dto.LikeResponseDTO;
import com.itda.moamoa.domain.like.entity.Like;
import com.itda.moamoa.domain.like.repository.LikeRepository;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service                    // Service Annotation
public class LikeApiService {
    @Autowired              // 의존성 주입 Annotation
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    // 좋아요 수 조회

    // 좋아요 생성
    @Transactional                                                              // Transaction Annotation
    public LikeResponseDTO create(LIkeRequestDTO requestDTO){
        // 1. User / Post 조회
        User userId = userRepository.findById(requestDTO.getUser_id())      // 예외처리 1. 존재하지 않는 사용자의 좋아요 생성 요청
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Post postId = postRepository.findById(requestDTO.getPost_id())      // 예외처리 2. 존재하지 않는 게시물에 좋아요 생성 요청
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        // 2. 좋아요 중복 확인
        if (likeRepository.existsById(userId, postId)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }

        // 3. 좋아요 Entity 생성 - Builder 적용
        Like like = Like.builder()
                .user(userId)
                .post(postId)
                .build();

        // 4. 좋아요 Entity -> Repository 저장
        Like createdLike = likeRepository.save(like);

        // 5. LikeResponseDTO 생성 및 반환
        return new LikeResponseDTO(createdLike.getLike_id(), createdLike.getUser().getId(), createdLike.getPost().getPost_id());
    }

    // 좋아요 삭제
//    @Transactional      // Transaction Annotation
//    public LikeResponseDTO delete(LIkeRequestDTO requestDto) {
//        // 1. User / Post 조회
//
//        // 2. Like Entity 불러오기
//        Like deletedLike = likeRepository.findById(requestDto.getLike_id()).orElseThrow("")
//
//        // 3. 좋아요 삭제
//        likeRepository.delete();
//    }
}
