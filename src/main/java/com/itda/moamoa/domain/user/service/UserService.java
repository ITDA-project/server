package com.itda.moamoa.domain.user.service;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.email.PasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(User user){
        user.encodingPassword(passwordEncoder);
        userRepository.save(user);
    }

    /*탈퇴한 이메일이라면 다시 사용 가능하게 false 반환*/
    public Boolean checkEmail(String email){
        Boolean exist = userRepository.existsByEmail(email);
        if(exist) { //존재한다면?
            if (!userRepository.findDeleteFlagByEmail(email).getDeleteFlag()) { //delete 가 false 라면 사용할 수 없음
                return false;
            }
            //존재하지만 탈퇴한 유저일 경우
            return true;
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
        User user = userRepository.findByUsername(passwordDto.getEmail());
        user.encodingPassword(passwordEncoder, passwordDto.getPassword());
    }
}
