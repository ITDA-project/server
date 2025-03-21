package com.itda.moamoa.domain.post.entity;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.BaseEntity;
import com.itda.moamoa.domain.post.entity.Category;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor //(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor //(access = AccessLevel.PRIVATE)
@Table(name = "posts")
@ToString
public class Post extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Integer membersMax;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Builder.Default
    private Integer likesCount = 0;

    @Builder.Default
    private Integer commentsCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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