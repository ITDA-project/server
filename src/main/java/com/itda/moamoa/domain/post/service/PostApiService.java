package com.itda.moamoa.domain.post.service;

import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import jakarta.annotation.PostConstruct;



@Service
@RequiredArgsConstructor
public class PostApiService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupMapper() { //response에 postId 받아오도록
        modelMapper.createTypeMap(Post.class, PostResponseDTO.class)
                .addMapping(Post::getPost_id, PostResponseDTO::setId);
    }

    // 게시글 전체 조회
    public List<PostResponseDTO> getAllPosts(String username) {
        // 1. 모든 게시글 조회
        List<Post> posts = postRepository.findAll();
        
        // 2. PostResponseDTO 리스트로 변환
        return posts.stream()
                .map(post -> modelMapper.map(post, PostResponseDTO.class))
                .toList();
    }

    // 게시글 조건 조회


    // 게시글 개별 조회
    public PostResponseDTO getPostById(long postId){
        // 1. Post 조회
        Post post = postRepository.findById(postId)             
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        
        // 2. PostResponseDTO로 변환하여 반환
        return modelMapper.map(post, PostResponseDTO.class);
    }


    // 게시글 생성
    @Transactional      // Transaction
    public Long create(String username, PostRequestDTO requestDto) {
        
        // 1. User 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));  

        // 2. Post Entity 생성
        Post post = modelMapper.map(requestDto, Post.class);
        post.setUser(user);     // 연관 관계

        // 3. DB 저장
        Post createdPost = postRepository.save(post);

        // 4. Post ID 반환
        return createdPost.getPost_id();
    }

    // 게시글 수정
    @Transactional
    public Long update(String username, long postId, PostRequestDTO requestDto){
        // 1. 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 3. 작성자 검증
        if (!post.getUser().equals(user)) {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }

        // 4. 게시글 수정
        post.changeTitle(requestDto.getTitle());
        post.changeContent(requestDto.getContent());
        post.changeCategory(requestDto.getCategory());
        post.changeMembersMax(requestDto.getMembersMax());
        post.changeLocation(requestDto.getLocation());

        // 5. 수정된 게시글 ID 반환
        return post.getPost_id();
    }


    // 게시글 삭제
    @Transactional
    public Long delete(String username, long postId, PostRequestDTO requestDto){
        // 1. User 조회
        User user = userRepository.findByUsername(username)  // 예외처리 1. 존재하지 않는 사용자의 게시물 생성 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 3. 작성자 검증
        if (!post.getUser().equals(user))                 // 게시글 작성한 사용자 != 게시글 삭제를 요청한 사용자
            throw new IllegalStateException("게시글을 삭제할 권한이 없습니다.");

        // 4. 게시글 삭제 - 실제 DB에서 삭제되지 않음
        post.softDelete();

        // 5. 삭제된 게시글 ID 반환
        return post.getPost_id();
    }
}
