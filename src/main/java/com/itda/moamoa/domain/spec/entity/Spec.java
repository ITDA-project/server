package com.itda.moamoa.domain.spec.entity;

import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "spec")
public class Spec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spec_id")
    private long spec_id;

    @ManyToOne                                          // Spec:User = n:1
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(name = "career", nullable = false)
    private String career;
}