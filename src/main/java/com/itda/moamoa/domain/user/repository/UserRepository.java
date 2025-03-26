package com.itda.moamoa.domain.user.repository;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.entity.UserDeleteFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
    User findByUsername(String username);
    Boolean existsByEmail(String email);
    UserDeleteFlag findDeleteFlagByEmail(String email);
}
