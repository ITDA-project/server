package com.itda.moamoa.domain.user.service;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.email.dto.PasswordDto;
import com.itda.moamoa.global.security.jwt.entity.Refresh;
import com.itda.moamoa.global.security.jwt.repository.RefreshRepository;
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

    public void createUser(User user){
        user.encodingPassword(passwordEncoder);
        userRepository.save(user);
    }

    public void createSocialUser(User user, String refresh) {
        User findUser = userRepository.findByUsername(user.getUsername());
        if (findUser == null) {
            user.encodingPassword(passwordEncoder);
            userRepository.save(user);
        }
        addRefresh(user.getUsername(),refresh,86400000L);
    }

    /*탈퇴한 이메일이라면 다시 사용 가능하게 false 반환*/
    public Boolean checkEmail(String email){
        Boolean exist = userRepository.existsByEmail(email);
        if(exist) { //존재한다면?
            if (!userRepository.findDeleteFlagByEmail(email).getDeleteFlag()) { //delete 가 false 라면 사용할 수 없음
                return false;
            }
        }
        //존재하지 않는 이메일
        return true;
    }

    /*soft delete 실행*/
    public void deleteUser(User user){
        user.softDelete();
        userRepository.save(user);
    }

    /*비밀번호 찾기에서 사용*/
    public void changePassword(PasswordDto passwordDto) {
        User user = userRepository.findByEmail(passwordDto.getEmail()).orElseThrow();
        user.encodingPassword(passwordEncoder, passwordDto.getPassword());
    }

    private void addRefresh(String username, String refresh, Long expiredMs){
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
