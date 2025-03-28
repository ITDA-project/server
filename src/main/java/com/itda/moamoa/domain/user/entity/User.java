package com.itda.moamoa.domain.user.entity;

import com.itda.moamoa.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@ToString
@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)  // User Only One e-mail
    private String email;   // varchar

    @Column(unique = false) // 중복 가능
    private String name;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private SnsDiv snsDiv;

    @Column
    private String phonenumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String image;           //image url
}
