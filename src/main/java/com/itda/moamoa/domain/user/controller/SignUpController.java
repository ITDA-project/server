package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.entity.UserDto;
import com.itda.moamoa.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SignUpController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @GetMapping("/auth/signup/email")
    public String getSingUpEmail() {
        return "auth/signup/email";
    }

    /**
     * 회원가입 메서드
     * username,snsDiv,role,image 미설정
     * image 는 회원 수정에서 등록
     */
    @PostMapping("/auth/signup/email")
    public String signUpEmail(UserDto userDto){
        User user = modelMapper.map(userDto,User.class);
        user.encodingPassword(passwordEncoder);
        userService.createUser(user);
        //성공 시 메인화면으로 이동
        return "redirect:/";
    }

    /**
     * 중복확인 메서드
     * 반환타입 결정 필요
     */
    @PostMapping("/auth/signup/email/checkemail")
    @ResponseBody
    public Boolean checkEmail(String email) {
        Boolean check = userService.checkEmail(email);
        return check;
    }


}
