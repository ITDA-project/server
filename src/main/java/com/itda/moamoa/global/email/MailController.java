package com.itda.moamoa.global.email;

import com.itda.moamoa.domain.user.service.UserService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import com.itda.moamoa.global.email.dto.EmailDto;
import com.itda.moamoa.global.email.dto.OtpDto;
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
     * 탈퇴한 유저, 소셜로그인 유저, DB에 존재하지 않는 이메일로 인증번호 전송 불가능
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
     * 인증번호 검증 메서드
     * email,otp 필요
     * 맞으면 true, 아니면 false
     */
    @PostMapping("/auth/password/otp")
    public ResponseEntity<ApiResponse<Boolean>> checkOtp(@RequestBody OtpDto otpDto){
        Boolean check = mailService.checkOtp(otpDto);
        ApiResponse<Boolean> response = ApiResponse.success(
                SuccessCode.OK,
                "인증번호 검증에 성공하였습니다.",
                check
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 비밀번호 변경 메서드
     * email,password 필요
     */
    @PatchMapping("/auth/password/find")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDto passwordDto) {
        userService.changePassword(passwordDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
