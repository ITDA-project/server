package com.itda.moamoa.domain.somoim.repository;

import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SomoimRepository extends JpaRepository<Somoim, Long> {
    Optional<Somoim> findByPost(Post oldPost);
}
