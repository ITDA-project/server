package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.entity.UserDto;
import com.itda.moamoa.domain.user.service.UserService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignUpController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    //나중에 삭제
    @GetMapping("/auth/signup/email")
    public String getSingUpEmail() {
        return "auth/signup/email";
    }

    //회원가입 테스트용 메서드, 나중에 삭제
    @GetMapping("/auth/login")
    @ResponseBody
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
    public String signUpEmail(@RequestBody UserDto userDto){
        //username, role 설정
        userDto.createAuthenticate();

        User user = modelMapper.map(userDto,User.class);
        user.encodingPassword(passwordEncoder);
        userService.createUser(user);
        //성공 시 로그인 화면으로 이동
        return "redirect:/api/auth/login";
    }


    @PostMapping("/auth/signup/{social}")
    public String singUpSocial(){
        //1.이미 등록된 이메일인지 확인 (카카오는 이메일이 아닌 username 으로 확인?)
        //1-1. 이미 등록되었다면 오류 오류 난 다음엔 어떻게 할 것인지?

        //2. username, 임의의 비밀번호 설정

        //3. DB에 저장

        //4. JWT(access token,refresh token) 를 만들어서 클라이언트에 반환

        //5. 로그인으로 리다이렉트?
        return "redirect:/";
    }

    /**
     * 이메일 중복확인 메서드
     */
    @PostMapping("/auth/signup/email/checkemail")
    @ResponseBody
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
