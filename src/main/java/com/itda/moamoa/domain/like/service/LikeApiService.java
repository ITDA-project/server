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
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LikeApiService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    // 좋아요 수 조회
    public Integer getLikeCount(long postId) {
        Post post = postRepository.findById(postId).orElse(null);

        return post.getLikesCount();
    }

    // 좋아요 생성
    @Transactional
    public LikeResponseDTO create(String username, long postId, LIkeRequestDTO requestDto){
        User user = userRepository.findByUsername(username)      // 예외처리 1. 존재하지 않는 사용자의 좋아요 생성 요청
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Post post = postRepository.findById(postId)             // 예외처리 2. 존재하지 않는 게시물에 좋아요 생성 요청
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        if (likeRepository.existsByUserAndPost(user, post)) {   // 예외처리 3. 이미 해당 게시물에 좋아요를 누른 경우
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }

        Like like = modelMapper.map(requestDto, Like.class);
        like.setUser(user);     // 연관 관계
        like.setPost(post);

        Like createdLike = likeRepository.save(like);
        post.plusLikeCount();

        return modelMapper.map(createdLike, LikeResponseDTO.class);
    }

    // 좋아요 삭제
    @Transactional
    public LikeResponseDTO delete(String username, long likeId, LIkeRequestDTO requestDto) {
        User uesr = userRepository.findByUsername(username)     // 예외처리 1. 권한이 없는 사용자의 좋아요 삭제 요청
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 삭제할 권한이 없습니다."));
        Like like = likeRepository.findById(likeId)             // 예외처리 2. 해당 게시글에 좋아요를 누르지 않은 경우
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글에 좋아요를 누르지 않으셨습니다."));
        Post post = like.getPost();

        likeRepository.delete(like);
        post.minusLikeCount();

        return modelMapper.map(like, LikeResponseDTO.class);
    }
}
