package com.itda.moamoa.domain.review.entity;

import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private Double star;

    private String sentence;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;  // 리뷰 대상 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;    // 리뷰 작성자

    public Review(Double star, String sentence, User targetUser, User reviewer) {
        this.star = star;
        this.sentence = sentence;
        this.targetUser = targetUser;
        this.reviewer = reviewer;
        this.createdAt = LocalDateTime.now();
    }
}