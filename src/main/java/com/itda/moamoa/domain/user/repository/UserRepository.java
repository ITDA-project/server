package com.itda.moamoa.domain.user.repository;

import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository                                                             // Repository
public interface UserRepository extends JpaRepository<User, Long> {     // JPA 상속
    Optional<User> findByUsername(String username);                     // JPA Query 자동 생성
}                                                                       // DB에서 E-mail을 찾아 반환