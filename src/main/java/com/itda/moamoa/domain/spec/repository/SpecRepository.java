package com.itda.moamoa.domain.spec.repository;

import com.itda.moamoa.domain.spec.entity.Spec;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecRepository extends JpaRepository<Spec, Long> {
    // User 엔티티로 Spec 찾기
    Optional<Spec> findByUser(User user);
}
