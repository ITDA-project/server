package com.itda.moamoa.domain.user.entity;

import com.itda.moamoa.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Getter
@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true) //한 사용자는 한 이메일 사용
    private String email; //varchar

    private String name; //한 사용자는 동일한 이름을 가질 수 있음(본명)

    @Column(unique = true)
    private String username; //username(유일), password로 로그인

    private String role;

    private String password;

    @Enumerated(EnumType.STRING)
    private SnsDiv snsDiv; //naver, kakao

    private String phonenumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String image; //image url

    public void encodingPassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }
}
