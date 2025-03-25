package com.itda.moamoa.domain.review.entity;

import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity                                             // Entity
@Getter                                             // Getter 자동 생성
@ToString                                           // ToString Override
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 생성
public class Review {
    @Id                                                 // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 자동 생성
    @Column(name = "review_id")
    private Long id;

    @Column
    private Double star;

    @Column
    private String sentence;

    @Temporal(TemporalType.TIMESTAMP)   // 날짜 및 시간 저장
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)  // Review:User = n:1
    @JoinColumn(name = "user_id")       // Foreign Key
    private User user;

    // 생성자
    public Review(Double star, String sentence, User user) {
        this.star = star;
        this.sentence = sentence;
        this.user = user;
    }
}
