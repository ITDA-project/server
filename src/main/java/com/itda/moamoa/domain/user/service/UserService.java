package com.itda.moamoa.domain.user.service;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.email.dto.PasswordDto;
import com.itda.moamoa.global.exception.custom.UserException;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import com.itda.moamoa.global.security.jwt.entity.Refresh;
import com.itda.moamoa.global.security.jwt.repository.RefreshRepository;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public void createUser(User user){
        user.encodingPassword(passwordEncoder);
        userRepository.save(user);
    }

    public void createSocialUser(User user, String refresh) {
        user.encodingPassword(passwordEncoder);
        userRepository.save(user);
        addRefresh(user.getUsername(),refresh,86400000L);
    }

    /**
     * 존재하지 않는 이메일 -> 사용 가능
     * 존재하지만 탈퇴한 이메일 -> 사용 불가능, 예외 발생
     * 존재하고 탈퇴안한 이메일 -> 사용 불가능, 예외 발생
     */
    public void checkEmail(String email){
        Boolean exist = userRepository.existsByEmail(email);
        if(exist) { //존재한다면?
            if (userRepository.findDeleteFlagByEmail(email).getDeleteFlag()) { //존재하지만 탈퇴한 이메일
                throw new UserException("이미 탈퇴한 이메일입니다.");
            }else{                                                             //존재하고 탈퇴안한 이메일
                throw new UserException("사용 중인 이메일입니다.");
            }
        }
        //존재하지 않는 이메일
    }

    /*소셜로그인 username 중복에 사용*/
    public Boolean checkUsername(String username){
        return userRepository.existsByUsername(username);
    }

    /*soft delete 실행*/
    public void deleteUser(User user){
        user.softDelete();
        userRepository.save(user);
    }

    /*비밀번호 찾기에서 사용*/
    public void changePassword(PasswordDto passwordDto) {
        User user = userRepository.findByEmail(passwordDto.getEmail()).orElseThrow(()->new EntityNotFoundException("등록되지 않은 이메일입니다."));
        user.encodingPassword(passwordEncoder, passwordDto.getPassword());
    }

    public void deleteUserAndInvalidateToken(CustomUserDetails customUserDetails, String refreshToken) {
        // refresh 토큰 유효성 검사
        jwtUtil.isExpired(refreshToken);

        // 유효한 토큰의 payload category 확인
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            throw new IllegalArgumentException("refresh token 이 아닙니다.");
        }

        // DB에서 해당 refresh 토큰 확인
        if (!refreshRepository.existsByRefresh(refreshToken)) {
            throw new IllegalArgumentException("존재하지 않는 token 입니다.");
        }
        User user = userRepository.findByUsername(customUserDetails.getUsername()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 회원입니다."));
        // 유저 삭제 처리
        deleteUser(user);
        // 토큰 무효화
        refreshRepository.deleteByRefresh(refreshToken);
    }


    public void addRefresh(String username, String refresh, Long expiredMs){
        //만료일자
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refresh1 = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refresh1);
    }
}
