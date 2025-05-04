package com.itda.moamoa.domain.post.service;

import com.itda.moamoa.domain.participant.repository.ParticipantRepository;
import com.itda.moamoa.domain.post.dto.PostListResponseDTO;
import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.participant.entity.*;
import com.itda.moamoa.domain.post.entity.Category;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.somoim.entity.SomoimStatus;
import com.itda.moamoa.domain.somoim.repository.SomoimRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import jakarta.annotation.PostConstruct;
import com.itda.moamoa.domain.spec.repository.SpecRepository;
import com.itda.moamoa.domain.like.repository.LikeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostApiService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ParticipantRepository participantRepository;
    private final SomoimRepository somoimRepository;
    private final SpecRepository specRepository;
    private final LikeRepository likeRepository;

    @PostConstruct
    public void setupMapper() { //response에 postId 받아오도록
        modelMapper.createTypeMap(Post.class, PostResponseDTO.class)
                .addMapping(Post::getPostId, PostResponseDTO::setId);
    }

    // 게시글 생성
    @Transactional
    public Long create(String username, PostRequestDTO requestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));  

        Post post = modelMapper.map(requestDto, Post.class);
        post.setUser(user);     // 연관 관계
        // 게시글 작성자(organizer)도 참가자로 계산하여 초기값 1 설정
        post.incrementParticipantCount();
        Post createdPost = postRepository.save(post);

        // 채팅방에서 사용
        Somoim somoim = new Somoim();
        somoim.setSomoim(SomoimStatus.BEFORE);
        somoimRepository.save(somoim);

        Participant participant = Participant.builder()
                .user(user)
                .somoim(somoim)
                .post(createdPost)
                .role(Role.ORGANIZER)
                .participantStatus(ParticipantStatus.ENTER)
                .build();
        participantRepository.save(participant);

        return createdPost.getPostId();
    }

    // 게시글 수정
    @Transactional
    public Long update(String username, long postId, PostRequestDTO requestDto){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getUser().equals(user)) {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }

        post.changeTitle(requestDto.getTitle());
        post.changeContent(requestDto.getContent());
        post.changeCategory(requestDto.getCategory());
        post.changeMembersMax(requestDto.getMembersMax());
        post.changeLocation(requestDto.getLocation());
        post.changeWarranty(requestDto.getWarranty());
        post.changeDueDate(requestDto.getDueDate());
        post.changeActivityStartDate(requestDto.getActivityStartDate());
        post.changeActivityEndDate(requestDto.getActivityEndDate());

        return post.getPostId();
    }


    // 게시글 삭제
    @Transactional
    public Long delete(String username, long postId){
        User user = userRepository.findByUsername(username)  // 예외처리 1. 존재하지 않는 사용자의 게시물 생성 요청
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getUser().equals(user))                 // 게시글 작성한 사용자 != 게시글 삭제를 요청한 사용자
            throw new IllegalStateException("게시글을 삭제할 권한이 없습니다.");

        post.softDelete();

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
    
    // 내가 작성한 글 목록 조회 - MyPageService에서 구현된 getFullMyPage로 대체 가능
//    public List<PostListResponseDTO> getPostsByUserId(Long userId, Long cursor, int size) {
//        if (cursor == null || cursor <= 0) {
//            cursor = Long.MAX_VALUE;
//        }
//        
//        PageRequest pageRequest = PageRequest.of(0, size);
//        List<Post> posts = postRepository.findByUserIdAndPostIdLessThanOrderByCreatedAtDesc(userId, cursor, pageRequest);
//        
//        return posts.stream()
//                .map(this::convertToListDTO)
//                .toList();
//    }

    // 게시글 개별 조회
    public PostResponseDTO getPostById(long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        PostResponseDTO postResponseDTO = modelMapper.map(post, PostResponseDTO.class);
        
        // 작성자 정보 설정
        User user = post.getUser();
        postResponseDTO.setUserId(user.getId());
        postResponseDTO.setUserName(user.getName());
        postResponseDTO.setUserImage(user.getImage());
        
        // 작성자의 경력 정보 가져오기
        specRepository.findByUser(user).ifPresent(spec -> postResponseDTO.setUserCareer(spec.getCareer()));
        
        return postResponseDTO;
    }
    
    // 게시글 개별 조회 (사용자 정보 포함)
    public PostResponseDTO getPostById(long postId, String username){
        // 기존 메서드 호출하여 게시글 정보 가져오기
        PostResponseDTO postResponseDTO = getPostById(postId);
        
        // 로그인한 사용자인 경우에만 좋아요 여부 확인
        if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
            
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
            
            // LikeRepository를 통해 좋아요 여부 확인
            boolean isLiked = likeRepository.existsByUserAndPost(user, post);
            postResponseDTO.setLiked(isLiked);
        } else {
            // 로그인하지 않은 사용자는 항상 false
            postResponseDTO.setLiked(false);
        }
        
        return postResponseDTO;
    }

    // 내가 좋아요한 글 목록 조회 - MyPageService에서 구현된 getFullMyPage로 대체 가능
//    public List<PostListResponseDTO> getLikedPostsByUserId(Long userId, Long cursor, int size) {
//        // TODO: 좋아요 기능 구현 후 활성화
//        return List.of(); // 임시로 빈 목록 반환
//        
//        /*
//        if (cursor == null || cursor <= 0) {
//            cursor = Long.MAX_VALUE;
//        }
//        
//        PageRequest pageRequest = PageRequest.of(0, size);
//        List<Post> posts = postRepository.findByLikesUserIdAndPostIdLessThanOrderByCreatedAtDesc(userId, cursor, pageRequest);
//        
//        return posts.stream()
//                .map(this::convertToListDTO)
//                .toList();
//        */
//    }

    // 게시물 검색 및 조회
    public List<PostListResponseDTO> searchPostsByKeywords(Long cursor, String keyword, int size) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어는 비어 있을 수 없습니다.");
        }

        if (cursor == null || cursor <= 0) {
            cursor = Long.MAX_VALUE;
        }

        PageRequest pageRequest = PageRequest.of(0, size);

        List<Post> posts;

        posts = postRepository.searchByPostIdLessThanAndDeleteFlagFalseOrderByCreatedAtDesc(cursor, keyword, pageRequest);

        if (posts.isEmpty()){
            log.info("검색어 [{}]에 해당하는 결과가 없습니다.", keyword);
            return List.of();
        }

        return posts.stream()
                .map(this::convertToListDTO)
                .toList();
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
