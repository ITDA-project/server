package com.itda.moamoa.domain.form.entity;

import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor //(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor //(access = AccessLevel.PRIVATE)
@Table(name = "forms")
public class Form {

    @Id
    @GeneratedValue
    @Column(name = "form_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FormStatus formStatus = FormStatus.APPLY;

    @Column(nullable = false) //길이제한 걸거라서, 그래도 longtext로 할건지??
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;
}