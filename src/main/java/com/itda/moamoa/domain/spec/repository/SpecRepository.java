package com.itda.moamoa.domain.spec.repository;

import com.itda.moamoa.domain.spec.entity.Spec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecRepository extends JpaRepository<Spec, Long> {
}
