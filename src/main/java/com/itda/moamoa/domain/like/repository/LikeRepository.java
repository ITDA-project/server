package com.itda.moamoa.domain.like.repository;

import com.itda.moamoa.domain.like.entity.Like;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository                                                             // Repository
public interface LikeRepository extends JpaRepository<Like, Long> {     // JPA 상속 -> CRUD, Pagging, JAP 지원
    boolean existsByUserAndPost(User user, Post post);      // if (userId != null && postId != null)
}