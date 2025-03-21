package com.itda.moamoa.domain.participant.repository;

import com.itda.moamoa.domain.participant.entitiy.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}