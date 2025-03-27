package com.itda.moamoa.domain.user.service;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private UserRepository userRepository;

    public String findNameByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user != null){
            return user.getName(); //사용자 본명 반환
        }

        return null; //사용자 없을 경우
    }
}
