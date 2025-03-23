package com.itda.moamoa.domain.form.entity;

import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Entity                 // Entity Annotation
@Getter                 // Getter 생성 Annotation
@Builder                // Builder Patter 제공 Annotation
@NoArgsConstructor      // (access = AccessLevel.PROTECTED) -> 외부에서 생성 불가능
@AllArgsConstructor     // (access = AccessLevel.PRIVATE) -> 외부에서 생성 불가능
@Table(name = "forms")
public class Form {
    @Id                         // Primary Key Annotation
    @GeneratedValue             // PK 자동 생성 Annotation
    @Column(name = "form_id")   // Attribute Annotation
    private Long id;

    @Enumerated(EnumType.STRING)    // Enum Type -> DB 저장 방식 지정 Annotation
    @Builder.Default                // Builder Annotation -> 필드에 기본값 (APPLY) 부여
    private FormStatus formStatus = FormStatus.APPLY;

    @Column(nullable = false)
    private String content;

    @ManyToOne                      // Form:Post = n:1
    @JoinColumn(name = "post_id")   // Foreign Key Annotation
    private Post post;

    @ManyToOne                      // Form:User = n:1
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;
}