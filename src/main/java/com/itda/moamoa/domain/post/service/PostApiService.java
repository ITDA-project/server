package com.itda.moamoa.domain.post.service;

import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service        // Service Annotation
public class PostApiService {
    @Autowired      // 의존성 주입 Annotation
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    // 게시글 전체 조회 -> 목록 조회

    // 게시글 개별 조회

    // 게시글 생성
    public PostResponseDTO create(PostRequestDTO requestDTO) {
        // 1. User 조회
        User userId = userRepository.findById(requestDTO.getUser_id())  // 예외처리 1. 존재하지 않는 사용자의 포스트 생성 요청
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. Post Entity 생성
        Post post = Post.builder()
                .user(userId)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .category(requestDTO.getCategory())
                .membersMax(requestDTO.getMambersMax())
                .location(requestDTO.getLocation())
                .dueDate(requestDTO.getDueDate())
                .build();

        // 4. Post Entity -> Repository 저장
        Post cretedPost = postRepository.save(post);

        // 5. PostResponseDTO 생성 및 반환
        return new PostResponseDTO(cretedPost.getPost_id(), cretedPost.getUser().getId(), cretedPost.getTitle(),
                cretedPost.getContent(), cretedPost.getCategory() , cretedPost.getMembersMax(), cretedPost.getLocation(), cretedPost.getDueDate());
    }

    // 게시글 수정


    // 게시글 삭제


}
