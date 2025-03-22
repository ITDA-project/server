package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/auth/delete")
    public String userDelete(@AuthenticationPrincipal User user){
        userService.deleteUser(user);

        return "redirect:/";
    }
}
