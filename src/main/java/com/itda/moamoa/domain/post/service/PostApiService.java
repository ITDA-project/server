package com.itda.moamoa.domain.post.service;

import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service                    // Service
@RequiredArgsConstructor    // 필수 필드 생성자
public class PostApiService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 게시글 전체 조회 -> 목록 조회

    // 게시글 개별 조회

    // 게시글 생성
    public PostResponseDTO create(PostRequestDTO requestDto) {
        // 1. User 조회
        User userId = userRepository.findById(requestDto.getUser())  // 예외처리 1. 존재하지 않는 사용자의 포스트 생성 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. Post Entity 생성
        Post post = modelMapper.map(requestDto, Post.class);

        // 3. DB 저장
        Post cretedPost = postRepository.save(post);

        // 5. PostResponseDTO 생성 및 반환
        return modelMapper.map(cretedPost, PostResponseDTO.class);
    }

    // 게시글 수정


    // 게시글 삭제


}
