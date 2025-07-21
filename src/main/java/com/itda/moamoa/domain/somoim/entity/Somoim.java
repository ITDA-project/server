package com.itda.moamoa.domain.somoim.entity;

import com.itda.moamoa.domain.participant.entity.Participant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Somoim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "somoim_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SomoimStatus status;

    @OneToMany(mappedBy = "somoim") // Participant:Somoim = 1:n
    List<Participant> participant = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    public void setSomoim(SomoimStatus status) { this.status = status; }
}