package com.itda.moamoa.domain.post.service;

import com.itda.moamoa.domain.post.dto.PostListResponseDTO;
import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.post.entity.Category;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
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
                .addMapping(Post::getPostId, PostResponseDTO::setId);
    }

    // 게시글 전체 조회


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
        return createdPost.getPostId();
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
        return post.getPostId();
    }


    // 게시글 삭제
    @Transactional
    public Long delete(String username, long postId){
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
        return post.getPostId();
    }

    // 커서 기반 페이지네이션 - 글 목록 조회
    public List<PostListResponseDTO> getPostsByCursor(Long cursor, Category category, String sort, int size) {
        // 초기 요청인 경우 가장 큰 ID 값 설정
        if (cursor == null || cursor <= 0) {
            cursor = Long.MAX_VALUE;
        }
        
        // 페이지 사이즈 설정
        PageRequest pageRequest = PageRequest.of(0, size);
        
        // 정렬 기준과 카테고리에 따라 다른 메서드 호출
        List<Post> posts;
        if ("likesCount".equals(sort)) {
            if (category != null) {
                posts = postRepository.findByPostIdLessThanAndCategoryAndDeleteFlagFalseOrderByLikesCountDescCreatedAtDesc(cursor, category, pageRequest);
            } else {
                posts = postRepository.findByPostIdLessThanAndDeleteFlagFalseOrderByLikesCountDescCreatedAtDesc(cursor, pageRequest);
            }
        } else {
            // 기본은 최신순
            if (category != null) {
                posts = postRepository.findByPostIdLessThanAndCategoryAndDeleteFlagFalseOrderByCreatedAtDesc(cursor, category, pageRequest);
            } else {
                posts = postRepository.findByPostIdLessThanAndDeleteFlagFalseOrderByCreatedAtDesc(cursor, pageRequest);
            }
        }
        
        // PostListResponseDTO로 변환
        return posts.stream()
                .map(this::convertToListDTO)
                .toList();
    }
    
    // 내가 작성한 글 목록 조회
    public List<PostListResponseDTO> getPostsByUserId(Long userId, Long cursor, int size) {
        if (cursor == null || cursor <= 0) {
            cursor = Long.MAX_VALUE;
        }
        
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Post> posts = postRepository.findByUserIdAndPostIdLessThanOrderByCreatedAtDesc(userId, cursor, pageRequest);
        
        return posts.stream()
                .map(this::convertToListDTO)
                .toList();
    }
    
    // 내가 좋아요한 글 목록 조회
    public List<PostListResponseDTO> getLikedPostsByUserId(Long userId, Long cursor, int size) {
        // TODO: 좋아요 기능 구현 후 활성화
        return List.of(); // 임시로 빈 목록 반환
        
        /*
        if (cursor == null || cursor <= 0) {
            cursor = Long.MAX_VALUE;
        }
        
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Post> posts = postRepository.findByLikesUserIdAndPostIdLessThanOrderByCreatedAtDesc(userId, cursor, pageRequest);
        
        return posts.stream()
                .map(this::convertToListDTO)
                .toList();
        */
    }
    
    // Post 엔티티를 PostListResponseDTO로 변환하는 헬퍼 메소드
    private PostListResponseDTO convertToListDTO(Post post) {
        return new PostListResponseDTO(
            post.getPostId(),
            post.getTitle(),
            post.getLikesCount(),
            post.getCreatedAt()
        );
    }
}
