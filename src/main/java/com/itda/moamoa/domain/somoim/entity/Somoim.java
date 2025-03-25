package com.itda.moamoa.domain.somoim.entity;

import com.itda.moamoa.domain.participant.entity.Participant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Entity                                             // Entity
@Getter                                             // Getter 자동 생성
@ToString                                           // ToString Override
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 생성
public class Somoim {
    @Id                                                 // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 자동 생성
    @Column(name = "somoim_id")
    private Long id;

    @Enumerated(EnumType.STRING)    // Enum Type
    private SomoimStatus status;

    @OneToMany(mappedBy = "somoim") // Participant:Somoim = 1:n
    List<Participant> participant = new ArrayList<>();

    // Setter
    public void setSomoim(SomoimStatus status) { this.status = status; }
}