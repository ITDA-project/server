package com.itda.moamoa.global.security.jwt.controller;

import com.itda.moamoa.global.security.jwt.dto.JoinDTO;
import com.itda.moamoa.global.security.jwt.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody //api server
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService){ //@Autowired 필드 주입 말고 생성자 주입
        this.joinService = joinService;
    }
    
    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO){
        joinService.joinProcess(joinDTO);
        return "ok";
    }
}
