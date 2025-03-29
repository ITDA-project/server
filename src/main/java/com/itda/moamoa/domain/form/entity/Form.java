package com.itda.moamoa.domain.form.entity;

import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "forms")
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id")
    private Long form_id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FormStatus formStatus = FormStatus.APPLY;

    @Column(nullable = false)
    private String content;

    @ManyToOne                      // Form:Post = n:1
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne                      // Form:User = n:1
    @JoinColumn(name = "user_id")
    private User user;

    // Setter
    public void setUser(User user) { this.user = user; }

    public void setPost(Post post) { this.post = post; }
}