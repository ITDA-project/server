package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.dto.SocialUserDto;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.entity.dto.UserDto;
import com.itda.moamoa.domain.user.service.UserService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import com.itda.moamoa.global.email.dto.EmailDto;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignUpController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;
    
    /**
     * 자체 회원가입 메서드
     * snsDiv,image 미설정
     * image 는 회원 수정에서 등록
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
    public ResponseEntity<?> singUpSocial(@RequestBody SocialUserDto socialUserDto, @PathVariable String social, HttpServletResponse response) throws IOException {
        //1. username, 임의의 비밀번호 설정
        socialUserDto.changeToUserForm(social);
        User user = modelMapper.map(socialUserDto, User.class);

        //2. access, refresh token 생성
        String access = jwtUtil.createJwt("access", user.getUsername(), user.getRole(), 36000000L); //1시간
        String refresh = jwtUtil.createJwt("refresh", user.getUsername(), user.getRole(), 864000000L); //10일

        //3. User,Refresh Token DB에 저장
        userService.createSocialUser(user,refresh);
        //4. JWT(access token,refresh token) 를 클라이언트에 반환
        response.setHeader("Authorization", "Bearer " + access);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); //인코딩
        response.getWriter().write("{\"refresh_token\": \"" + refresh + "\"}");

        //5. 메인 화면으로 리다이렉트
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("http://localhost:8080/api/home"));

        return new ResponseEntity<>(httpHeaders,HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * 이메일 중복확인 메서드
     */
    @PostMapping("/auth/signup/email/checkemail")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestBody EmailDto email) {

        userService.checkEmail(email.getEmail());
        ApiResponse<Boolean> response = ApiResponse.success(
                SuccessCode.OK,
                "이메일 중복 조회에 성공했습니다.",
                true
        );
        return ResponseEntity.ok(response);
    }


}
