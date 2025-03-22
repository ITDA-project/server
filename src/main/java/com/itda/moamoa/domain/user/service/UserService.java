package com.itda.moamoa.domain.user.service;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public void createUser(User user){
        userRepository.save(user);
    }

    public Boolean checkEmail(String email){
        return userRepository.existsByEmail(email);
    }

    /*soft delete 실행*/
    public void deleteUser(User user){
        user.softDelete();
        userRepository.save(user);
    }
}
