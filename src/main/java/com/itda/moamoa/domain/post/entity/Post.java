package com.itda.moamoa.domain.post.entity;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity                                                 // Entity
@ToString                                               // ToString Override
@Getter                                                 // Getter 자동 생성
@Builder                                                // Builder Pattern 지원
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 기본 생성자 생성
@AllArgsConstructor(access = AccessLevel.PRIVATE)       // 모든 필드 생성자
@Table(name = "posts")
public class Post extends BaseEntity{
    @Id                                                 // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 자동 생성
    @Column(name = "post_id")
    private long post_id;

    @ManyToOne(fetch = FetchType.LAZY)      // Post:User = n:1
    @JoinColumn(name = "user_id")           // Foreign Key
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)            // Enum Type
    private Category category;

    @Column(nullable = false)
    private Integer membersMax;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Builder.Default                    // Builder Pattern 적용 시 기본값 = 0
    private Integer likesCount = 0;

    @Builder.Default                    // Byilder Pattern 적용 시, 기본값 = 0
    private Integer commentsCount = 0;


    // Setter
    public void setUser(User user) { this.user = user; }

    // Update
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changeMembersMax(Integer membersMax) {
        this.membersMax = membersMax;
    }

    public void changeLocation(String location) {
        this.location = location;
    }
}