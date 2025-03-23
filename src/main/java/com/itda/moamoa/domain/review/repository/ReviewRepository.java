package com.itda.moamoa.domain.review.repository;

import com.itda.moamoa.domain.review.entity.Review;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTargetUser(User targetUser);
    List<Review> findByReviewer(User reviewer);
}
