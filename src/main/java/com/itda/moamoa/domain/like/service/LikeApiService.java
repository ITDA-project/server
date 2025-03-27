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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service                    // Service
@RequiredArgsConstructor    // 필수 필드 생성자
public class LikeApiService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    // 좋아요 수 조회
    // 그냥 카운트 해서 숫자만 반환
    public Like getAllLike(long postId) {
        // 1. Post 조회
        Post post = postRepository.findById(postId)     // 예외처리 1. 존재하지 않는 게시물에 좋아요 조회 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하즤 않는 게시물입니다."));

        // 2. 조회한 좋아요 수 반환
        return null;
    }

    // 좋아요 생성
    @Transactional                                                              // Transaction
    public LikeResponseDTO create(LIkeRequestDTO requestDto){
        // 1. User / Post 조회
        User user = userRepository.findById(requestDto.getUser())      // 예외처리 1. 존재하지 않는 사용자의 좋아요 생성 요청
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Post post = postRepository.findById(requestDto.getPost())      // 예외처리 2. 존재하지 않는 게시물에 좋아요 생성 요청
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        // 2. 좋아요 중복 확인
        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }

        // 3. RequestDTO -> 좋아요 Entity 변환 - ModelMapper 적용
        Like like = modelMapper.map(requestDto, Like.class);
        like.setUser(user);     // 연관 관계
        like.setPost(post);

        // 4. DB 저장
        Like createdLike = likeRepository.save(like);

        // 5. LikeResponseDTO 생성 및 반환 - ModelMapper 적용
        return modelMapper.map(createdLike, LikeResponseDTO.class);
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
