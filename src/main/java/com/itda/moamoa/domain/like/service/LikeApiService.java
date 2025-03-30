package com.itda.moamoa.domain.like.service;

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

    // 좋아요 생성
    @Transactional
    public Like create(String username, long postId){
        User user = userRepository.findByUsername(username)      // 예외처리 1. 존재하지 않는 사용자의 좋아요 생성 요청
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Post post = postRepository.findById(postId)             // 예외처리 2. 존재하지 않는 게시물에 좋아요 생성 요청
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        if (likeRepository.existsByUserAndPost(user, post)) {   // 예외처리 3. 이미 해당 게시물에 좋아요를 누른 경우
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }

        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();
        Like createdLike = likeRepository.save(like);
        post.plusLikeCount();

        return createdLike;
    }

    // 좋아요 삭제
    @Transactional
    public void delete(String username, long postId) {
        User user = userRepository.findByUsername(username)         // 예외처리 1. 권한이 없는 사용자의 좋아요 삭제 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Post post = postRepository.findById(postId)                 // 예외처리 2. 삭제된 게시물의 좋아요 삭제 요청
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        Like like = likeRepository.findByUserAndPost(user, post)   // 예외처리 3.
                .orElseThrow(() -> new IllegalStateException("해당 게시물에 좋아요를 누르지 않으셨습니다."));

        likeRepository.delete(like);
        post.minusLikeCount();
    }
}
