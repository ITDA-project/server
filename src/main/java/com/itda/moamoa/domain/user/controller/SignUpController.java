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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignUpController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;

    /**
     * 자체 회원가입 메서드
     * snsDiv, image 미설정
     * image 는 회원 수정에서 등록
     */
    @PostMapping("/auth/signup/email")
    public ResponseEntity<Object> signUpEmail(@RequestBody UserDto userDto) {
        userDto.createAuthenticate();
        User user = modelMapper.map(userDto, User.class);
        userService.createUser(user);

        // 프론트에서 화면 전환하므로 201 응답만 반환
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 회원가입에 실패하는 경우: 신규회원이며, 이미 가입된 이메일로 시도하는 경우
     * 회원가입과 로그인 구분: DB에 이미 존재하는 username(id)로 요청한 경우는 로그인, 아니면 회원가입
     * 1.username이 존재하는지 확인
     * 1-1. 존재한다면 로그인처리만 하고 return
     * 1-2. 존재하지 않으면 신규회원으로 2번으로 이동
     * 2.email이 존재하는지 확인
     * 2-1. 존재한다면 가입 불가능 예외 발생
     * 2-2. 존재하지 않으면 신규회원 가입 가능 3번으로 이동
     * 3.신규 회원가입 진행
     */
    @PostMapping("/auth/signup/{social}")
    public ResponseEntity<?> signUpSocial(@RequestBody SocialUserDto socialUserDto,
                                          @PathVariable("social") String social,
                                          HttpServletResponse response) throws IOException {
        socialUserDto.changeToUserForm(social);
        User user = modelMapper.map(socialUserDto, User.class);

        String access = jwtUtil.createJwt("access", user.getUsername(), user.getRole(), 36000000L);
        String refresh = jwtUtil.createJwt("refresh", user.getUsername(), user.getRole(), 864000000L);

        //1.
        if(userService.checkUsername(user.getUsername())){// 1-1.
            userService.addRefresh(user.getUsername(),refresh,86400000L);
            setResponseHeader(response,access,refresh);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        //2.
        userService.checkEmail(user.getEmail());

        //3.
        userService.createSocialUser(user, refresh);
        setResponseHeader(response, access, refresh);

        // 프론트에서 화면 전환하므로 201 응답만 반환
        return new ResponseEntity<>(HttpStatus.CREATED);
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

    private void setResponseHeader(HttpServletResponse response, String access, String refresh) throws IOException {
        response.setHeader("access", access);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"refresh_token\": \"" + refresh + "\"}");
    }
}