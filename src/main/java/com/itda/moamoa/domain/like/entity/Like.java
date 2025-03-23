package com.itda.moamoa.domain.like.entity;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.post.entity.Post;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity                 // Entity Annotation
@Getter                 // Getter 생성 Annotation
@Setter                 // Setter 생성 Annotation
@Builder                // Builder Patter 제공 Annatation
@NoArgsConstructor      // (access = AccessLevel.PROTECTED)
@AllArgsConstructor     // (access = AccessLevel.PRIVATE)
@Table(name = "likes")  // Table Annotation
public class Like {
    @Id                                                     // Primary Key Annotation
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // PK 자동 생성 Annotation
    @Column(name = "like_id", updatable = false)            // Attribute Annotation -> 변경 불가능
    private long like_id;

    @ManyToOne                                              // Like:Post = n:1
    @JoinColumn(name = "post_id", nullable = false)         // Foreign Key Annotation -> null 불가능
    private Post post;

    @ManyToOne                                              // Like:User = n:1
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false) 
    private LocalDateTime createdAt = LocalDateTime.now();  // 생성 날짜 자동 기입
}