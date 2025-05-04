package com.itda.moamoa.domain.like.repository;

import com.itda.moamoa.domain.like.entity.Like;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndPost(User user, Post post);
    
    // 특정 사용자가 좋아요한 모든 목록 조회
    List<Like> findByUser(User user);
}