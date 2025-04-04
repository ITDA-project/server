package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.Gender;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class UserControllerTest {

    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public UserControllerTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Test
    void userDelete() {
        User user = User.builder()
                .email("temp@spring.com")
                .gender(Gender.MALE)
                .name("spring")
                .password("1111")
                .build();

        userService.createUser(user);
        userService.deleteUser(user);

        User deletedUser = userRepository.findById(user.getId()).get();

        Assertions.assertThat(deletedUser.isDeleted()).isEqualTo(true);
    }
}