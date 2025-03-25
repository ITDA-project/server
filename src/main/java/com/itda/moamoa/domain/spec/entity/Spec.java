package com.itda.moamoa.domain.spec.entity;

import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity                                             // Entity
@Getter                                             // Getter자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 생성
@AllArgsConstructor
@Table(name = "spec")
public class Spec {
    @Id                                                     // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // PK 자동 생성
    @Column(name = "spec_id")
    private long spec_id;

    @ManyToOne                                          // Spec:User = n:1
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "career", columnDefinition = "LONGTEXT", nullable = false)
    private String career;
}