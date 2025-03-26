package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 수정 필요
     * 회원 탈퇴 메소드
     * 탈퇴 후엔 로그아웃이 필요 -> 토큰들 다 만료시키기
     */
    @DeleteMapping("/auth/delete")
    public String userDelete(@AuthenticationPrincipal(expression = "user") User user){
        userService.deleteUser(user);
        // logout 으로 redirect 해서 LogoutFilter에 걸리게 한다.
        return "redirect:/logout";
    }

    //비밀번호 찾기 메서드
    //이메일, 새 비밀번호, 인증번호(OTP) 받아오기
    //이메일로 인증번호 보내기 외부 API 사용
    //만약 회원가입 안한 이메일이라면?
    //social 로그인한 회원이라면?
    @PatchMapping("/auth/password")
    public String findPassword(){
        return "ok";
    }
}
