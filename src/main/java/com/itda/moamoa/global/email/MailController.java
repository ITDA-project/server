package com.itda.moamoa.global.email;

import com.itda.moamoa.domain.user.service.UserService;
import com.itda.moamoa.global.email.dto.EmailDto;
import com.itda.moamoa.global.email.dto.PasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final UserService userService;

    /**
     * email 이 소셜 로그인한 이메일이라면?
     */
    @PostMapping("/auth/password/find")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody EmailDto emailDto) {
        String email = emailDto.getEmail();

        //DB에 존재하고, 자체 회원가입한 사용자인지 확인
        mailService.checkExistEmail(email);

        //자체 회원가입한 사용자라면 인증번호 전송
        mailService.sendMail(email);
        Map<String, String> response = new HashMap<>();
        response.put("status","success");
        response.put("message", "인증 번호가 전송되었습니다.");

        return ResponseEntity.ok(response);
    }

    /**
     * 비밀번호 변경 메서드
     * 보낸 OTP 가 저장한 OTP 와 맞다면 true, 아니라면 false
     * 맞는 경우, login 으로 리다이렉트
     * 아닌 경우, OTP 가 맞지 않다고 응답
     */
    @PatchMapping("/auth/password/find")
    public ResponseEntity<Map<String,String>> changePassword(@RequestBody PasswordDto passwordDto) {
        Boolean check = mailService.checkOtp(passwordDto);
        HttpHeaders httpHeaders = null;
        if(check){
            userService.changePassword(passwordDto);
            httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create("http://localhost:8080/api/auth/login"));
            return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
        }else{
            Map<String, String> response = new HashMap<>();
            response.put("message", "인증 번호가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
