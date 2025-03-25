package com.itda.moamoa.domain.review.repository;

import com.itda.moamoa.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository                                                             // Repository
public interface ReviewRepository extends JpaRepository<Review, Long> { // JPA 상속
}