package com.itda.moamoa.domain.form.entity;

import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity                                              // Entity
@Getter                                              // Getter 생성
@Builder                                             // Builder Patter 제공 -> NoArgsConstructor 필수
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // 기본 생성자 -> 같은 패키지 / 하위 클래스까지 접근 허용
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 모든 필드 생성자 -> 해당 클래스 내에서만 접근 허용
@Table(name = "forms")
public class Form {
    @Id                                                     // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // PK 자동 생성
    @Column(name = "form_id")
    private Long form_id;

    @Enumerated(EnumType.STRING)    // EnumType
    @Builder.Default                // Builder Pattern 적용 -> 필드에 기본값 (APPLY) 부여
    private FormStatus formStatus = FormStatus.APPLY;

    @Column(nullable = false)
    private String content;

    @ManyToOne                      // Form:Post = n:1
    @JoinColumn(name = "post_id")   // Foreign Key
    private Post post;

    @ManyToOne                      // Form:User = n:1
    @JoinColumn(name = "user_id")
    private User user;

    // Setter
    public void setUser(User user) { this.user = user; }

    public void setPost(Post post) { this.post = post; }
}