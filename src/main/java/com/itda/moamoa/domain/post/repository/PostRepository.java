package com.itda.moamoa.domain.post.repository;

import com.itda.moamoa.domain.post.entity.Category;
import com.itda.moamoa.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 카테고리별 최신순 조회 (postId < cursor)
    List<Post> findByPostIdLessThanAndCategoryOrderByCreatedAtDesc(Long cursor, Category category, Pageable pageable);
    
    // 카테고리별 좋아요순 조회
    List<Post> findByPostIdLessThanAndCategoryOrderByLikesCountDescCreatedAtDesc(Long cursor, Category category, Pageable pageable);

    // 전체글 최신순 조회
    List<Post> findByPostIdLessThanOrderByCreatedAtDesc(Long cursor, Pageable pageable);
    
    // 전체글 좋아요순 조회
    List<Post> findByPostIdLessThanOrderByLikesCountDescCreatedAtDesc(Long cursor, Pageable pageable);
    
    //마이페이지용
    // 특정 사용자가 작성한 글 조회
    List<Post> findByUserIdAndPostIdLessThanOrderByCreatedAtDesc(Long userId, Long cursor, Pageable pageable);
    
    // 사용자가 좋아요한 글 조회 (가정)
    //findByLikesUserIdAndPostIdLessThanOrderByCreatedAtDesc

}