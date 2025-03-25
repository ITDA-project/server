package com.itda.moamoa.domain.like.entity;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.post.entity.Post;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity                 // Entity
@Getter                 // Getter 생성
@Builder                // Builder Patter 제공
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "likes")
public class Like {
    @Id                                                     // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // PK 자동 생성
    @Column(name = "like_id", updatable = false)            // 변경 불가능
    private long like_id;

    @ManyToOne(fetch = FetchType.LAZY)                      // Like:Post = n:1
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)                      // Like:User = n:1
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false) 
    private LocalDateTime createdAt;     // 생성 날짜 자동 기입

    // setter
    public void setUser(User user) { this.user = user; }

    public void setPost(Post post) { this.post = post; }
}