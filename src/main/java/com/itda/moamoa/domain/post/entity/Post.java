package com.itda.moamoa.domain.post.entity;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "posts")
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long post_id;

    @ManyToOne(fetch = FetchType.LAZY)      // Post:User = n:1
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Integer membersMax;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate dueDate;

    //@Column(nullable = false)
    private String warranty;

    //@Column(nullable = false)
    private LocalDate ActivityStartDate;

    @Column
    private LocalDate ActivityEndDate;

    @Builder.Default
    private Integer likesCount = 0;

    @Builder.Default
    private Integer participantCount = 0;


    public void setUser(User user) { this.user = user; }

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


    public void plusLikeCount() {   this.likesCount += 1; }
    public void minusLikeCount() {  this.likesCount -= 1; }
}