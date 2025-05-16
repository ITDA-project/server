package com.itda.moamoa.domain.somoim.repository;

import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SomoimRepository extends JpaRepository<Somoim, Long> {
    @Query("SELECT s FROM Somoim s JOIN Participant p ON s = p.somoim WHERE p.post.id = :postId")
    Optional<Somoim> findSomoimByPostId(@Param("postId") Long postId);


}
