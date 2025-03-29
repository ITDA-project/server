package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.Gender;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.entity.dto.UserDto;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.domain.user.service.UserService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class SignUpControllerTest {

    private SignUpController signUpController;
    private UserRepository userRepository;
    private UserService userService;
    private EntityManager em;

    @Autowired
    public SignUpControllerTest(SignUpController signUpController, UserRepository userRepository,UserService userService,EntityManager em) {
        this.signUpController = signUpController;
        this.userRepository = userRepository;
        this.userService = userService;
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
    void signUpDeletedUser() {
        User user1 = User.builder()
                .email("temp@spring.com")
                .gender(Gender.MALE)
                .name("spring")
                .password("1111")
                .build();
        User user2 = User.builder()
                .id(1L)
                .email("temp2@spring.com")
                .gender(Gender.FEMALE)
                .name("spring change")
                .password("11112222")
                .build();

        userService.createUser(user1);
        em.flush();
        userService.deleteUser(user1);
        em.flush();
        em.clear();
        /*탈퇴 후 같은 이메일로 재가입*/

        userService.createUser(user2);
        em.flush();
        em.clear();
        User findUser = userRepository.findById(1L).get();

        log.info("findUser={}",findUser);
        assertThat(findUser.isDeleteFlag()).isEqualTo(false);
    }

    //이메일이 이미 존재하는 상황
    @Test
    void checkEmail() {
        User user1 = User.builder()
                .email("temp@spring.com")
                .gender(Gender.MALE)
                .name("spring")
                .password("1111")
                .build();
        userService.createUser(user1);

        Boolean check = userService.checkEmail("temp@spring.com");
        assertThat(check).isEqualTo(false);
    }

    //이미 탈퇴한 회원의 이메일 확인
    @Test
    void checkEmailDeleteUser() {
        User user1 = User.builder()
                .email("temp@spring.com")
                .gender(Gender.MALE)
                .name("spring")
                .password("1111")
                .build();
        userService.createUser(user1);
        userService.deleteUser(user1);
        em.flush();

        Boolean check = userService.checkEmail("temp@spring.com");
        assertThat(check).isEqualTo(true);
    }
}