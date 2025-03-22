package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.Gender;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.entity.UserDto;
import com.itda.moamoa.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
@Slf4j
class SignUpControllerTest {

    private SignUpController signUpController;
    private UserRepository userRepository;
    private EntityManager em;

    @Autowired
    public SignUpControllerTest(SignUpController signUpController, UserRepository userRepository,EntityManager em) {
        this.signUpController = signUpController;
        this.userRepository = userRepository;
        this.em = em;
    }

    @Test
    void singUpEmail() {
        UserDto userDto = UserDto.builder()
                .email("temp@spring.com")
                .gender(Gender.MALE)
                .name("spring")
                .password("1111")
                .build();

        signUpController.signUpEmail(userDto);
        em.flush();
        User user = userRepository.findById(1L).get();

        log.info("user={}",user);
    }

    @Test
    void checkEmail() {
    }
}