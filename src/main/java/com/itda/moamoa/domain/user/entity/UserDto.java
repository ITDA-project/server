package com.itda.moamoa.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;

    private String email; //varchar

    private String name; //한 사용자는 동일한 이름을 가질 수 있음(본명)

    private String username; //username(유일), password로 로그인

    private String role;

    private String password;

    private SnsDiv snsDiv; //naver, kakao

    private String phonenumber;

    private Gender gender;
}
