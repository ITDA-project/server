package com.itda.moamoa.domain.spec.entity;

import com.itda.moamoa.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Spec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spec_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(name = "career", nullable = false)
    private String career;

    // 생성자를 통한 초기화
    private Spec(User user, String career) {
        this.user = user;
        this.career = career;
    }

    // 정적 팩토리 메서드
    public static Spec create(User user, String career) {
        return new Spec(user, career);
    }

    // career 업데이트 메서드
    public Spec updateCareer(String career) {
        this.career = career;
        return this;
    }
}