package com.itda.moamoa.domain.post.entity;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity                     // Entity Annotation
@ToString                   // ToString Override Annotation
@Getter                     // Getter 자동 생성 Annotation
@Builder                    // Builder Pattern 지원 Annotation
@NoArgsConstructor          // (access = AccessLevel.PROTECTED)
@AllArgsConstructor         // (access = AccessLevel.PRIVATE)
@Table(name = "posts")      // Table Annotation
public class Post extends BaseEntity{
    @Id                 // Primary Key Annotation
    @GeneratedValue     // PK 자동 생성 Annotation
    @Column(name = "post_id")
    private long post_id;

    @ManyToOne(fetch = FetchType.LAZY)      // Post:User = n:1
    @JoinColumn(name = "user_id")           // Foreign Key Annotation
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)            // Enum Type -> DB 저장 방식 지정 Annotation
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