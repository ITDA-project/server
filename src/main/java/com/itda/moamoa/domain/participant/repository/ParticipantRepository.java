package com.itda.moamoa.domain.participant.repository;

import com.itda.moamoa.domain.participant.entity.Participant;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Participant findByUserUsernameAndPost(String username, Post post); //user 객체 안의 username
}