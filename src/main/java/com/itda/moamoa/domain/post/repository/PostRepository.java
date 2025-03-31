package com.itda.moamoa.domain.post.repository;

import com.itda.moamoa.domain.post.entity.Category;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // 삭제되지 않은 게시글 조회 (deleteFlag=false)
    // 카테고리별 최신순 조회 (postId < cursor, deleteFlag=false)
    List<Post> findByPostIdLessThanAndCategoryAndDeleteFlagFalseOrderByCreatedAtDesc(Long cursor, Category category, Pageable pageable);
    
    // 카테고리별 좋아요순 조회 (deleteFlag=false)
    List<Post> findByPostIdLessThanAndCategoryAndDeleteFlagFalseOrderByLikesCountDescCreatedAtDesc(Long cursor, Category category, Pageable pageable);

    // 전체글 최신순 조회 (deleteFlag=false)
    List<Post> findByPostIdLessThanAndDeleteFlagFalseOrderByCreatedAtDesc(Long cursor, Pageable pageable);
    
    // 전체글 좋아요순 조회 (deleteFlag=false)
    List<Post> findByPostIdLessThanAndDeleteFlagFalseOrderByLikesCountDescCreatedAtDesc(Long cursor, Pageable pageable);

    // 마이페이지용 - PostApiController의 /my 엔드포인트에서 사용 (현재 주석 처리됨)
    // 특정 사용자가 작성한 글 조회
    // List<Post> findByUserIdAndPostIdLessThanOrderByCreatedAtDesc(Long userId, Long cursor, Pageable pageable);
    
    // 특정 사용자가 작성한 삭제되지 않은 게시글 모두 조회 - MyPageService에서 사용 중
    List<Post> findByUserAndDeleteFlagFalse(User user);
    
    // 사용자가 좋아요한 글 조회 (가정)
    //findByLikesUserIdAndPostIdLessThanOrderByCreatedAtDesc
}