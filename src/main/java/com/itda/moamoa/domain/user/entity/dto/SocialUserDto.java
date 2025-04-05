package com.itda.moamoa.domain.user.entity.dto;

import com.itda.moamoa.domain.user.entity.Gender;
import com.itda.moamoa.domain.user.entity.SnsDiv;
import com.itda.moamoa.global.common.RandomPasswordGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SocialUserDto {
    private String id;

    private String email; //varchar

    private String name; //한 사용자는 동일한 이름을 가질 수 있음(본명)

    private String username; //username(유일), password로 로그인

    private String role;

    private String password;

    private SnsDiv snsDiv; //naver, kakao

    private String phonenumber;

    private Gender gender;

    public void changeToUserForm(String social){
        this.username = social + " " + this.id;
        this.id = null;
        this.role = "ROLE_USER";
        this.password = RandomPasswordGenerator.generatePassword();
        this.snsDiv = SnsDiv.fromString(social);
    }
}
