package com.itda.moamoa.domain.user.entity;

import com.itda.moamoa.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity                 // Entity
@Getter                 // Getter 자동 생성
@Builder                // Builder 제공
@ToString               // ToString Override
@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 생성
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // 모든 필드 생성자 생성

public class User extends BaseEntity {
    @Id                                                     // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // PK 자동 생성
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)  // User Only One e-mail
    private String email;   // varchar

    @Column(unique = false) // 중복 가능
    private String name;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)    // Enum Type
    private SnsDiv snsDiv;

    @Column
    private String phonenumber;

    @Enumerated(EnumType.STRING)    // Enum Type
    private Gender gender;

    @Column
    private String image;           //image url
}
