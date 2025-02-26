package com.itda.moamoa.domain.somoim.entity;

import com.itda.moamoa.domain.participant.entitiy.Participant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class Somoim {

    @Id
    @GeneratedValue
    @Column(name = "somoim_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SomoimStatus status;

    //OneToMany 추가
    //participants
    @OneToMany(mappedBy = "somoim")
    List<Participant> participant = new ArrayList<>();

    public Somoim(SomoimStatus status) {
        this.status = status;
    }
}