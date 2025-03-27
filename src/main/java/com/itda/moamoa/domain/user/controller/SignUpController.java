package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.entity.UserDto;
import com.itda.moamoa.domain.user.service.UserService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignUpController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    //회원가입 테스트용 메서드, 나중에 삭제
    @GetMapping("/auth/login")
    public ResponseEntity<String> testSignUp(){
        return ResponseEntity.ok("ok");
    }

    /**
     * 자체 회원가입 메서드
     * snsDiv,image 미설정
     * image 는 회원 수정에서 등록
     * soft delete 되었던 유저라면 deleteFlag 도 수정됨(추후 다시 논의)
     */
    @PostMapping("/auth/signup/email")
    public ResponseEntity<Object> signUpEmail(@RequestBody UserDto userDto){
        //username, role 설정
        userDto.createAuthenticate();

        User user = modelMapper.map(userDto,User.class);
        userService.createUser(user);
        //성공 시 로그인 화면으로 이동
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:8080/api/auth/login"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }


    @PostMapping("/auth/signup/{social}")
    public String singUpSocial(){
        //1.이미 등록된 이메일인지 확인 (카카오는 이메일이 아닌 username 으로 확인?)
        //1-1. 이미 등록되었다면 오류 오류 난 다음엔 어떻게 할 것인지?

        //2. username, 임의의 비밀번호 설정

        //3. 이미 소셜로 회원가입한 사용자는 JWT token 만 만들어서 반환

        //4. DB에 저장

        //5. JWT(access token,refresh token) 를 만들어서 클라이언트에 반환

        //6. 메인 화면으로 리다이렉트? 사용자가 로그인 전에 봤던 곳으로 리다이렉트?
        return "redirect:/";
    }

    /**
     * 이메일 중복확인 메서드
     */
    @PostMapping("/auth/signup/email/checkemail")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(String email) {

        Boolean check = userService.checkEmail(email);
        ApiResponse<Boolean> response = ApiResponse.success(
                SuccessCode.OK,
                "이메일 중복 조회에 성공했습니다.",
                check
        );
        return ResponseEntity.ok(response);
    }


}
