package com.itda.moamoa.domain.spec.entity;

import com.itda.moamoa.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "spec")
@Getter
@Setter

public class Spec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spec_id")
    private long spec_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User post;

    @Column(name = "career", columnDefinition = "LONGTEXT", nullable = false)
    private String career;
}