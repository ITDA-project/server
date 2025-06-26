package com.itda.moamoa.domain.participant.repository;

import com.itda.moamoa.domain.participant.entity.Participant;
import com.itda.moamoa.domain.participant.entity.Role;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Participant findByUserUsernameAndPost(String username, Post post); //user 객체 안의 username
    Optional<Participant> findByUserAndPost(User user, Post post);
    Optional<Participant> findByPostAndRole(Post post, Role role);

    @Query("SELECT p.user FROM Participant p WHERE p.somoim = :somoim AND p.role = :role")
    User findBySomoimAndRole(@Param("somoim") Somoim somoim, @Param("role") Role role);
}