package com.itda.moamoa.domain.participants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Participants {
    @Id
    @GeneratedValue
    @Column(name = "participant_id")
    private Long id;
}
