package com.itda.moamoa.domain.review.entity;

import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private Double star;

    private String sentence;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Review(Double star, String sentence, User user) {
        this.star = star;
        this.sentence = sentence;
        this.user = user;
    }
}