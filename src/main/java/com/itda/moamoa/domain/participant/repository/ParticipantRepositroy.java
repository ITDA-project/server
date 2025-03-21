package com.itda.moamoa.domain.participant.repository;

import com.itda.moamoa.domain.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepositroy extends JpaRepository<Participant, Long> {
}
