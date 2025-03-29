package com.itda.moamoa.global.security.jwt.service;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.security.jwt.dto.JoinDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Boolean joinProcess(JoinDTO joinDTO){ //true false
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);
        if(isExist){
            return Boolean.FALSE; //username 중복
        }

        User user = User.builder()
                .username(username)
                //비밀번호 암호화 후 저장
                .password(bCryptPasswordEncoder.encode(password))
                .role("ROLE_ADMIN").build(); //기본 role ADMIN

        userRepository.save(user);
        return Boolean.TRUE;
    }




}
