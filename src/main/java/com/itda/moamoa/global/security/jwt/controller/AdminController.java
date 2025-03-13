package com.itda.moamoa.global.security.jwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class AdminController {
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
}
