package com.itda.moamoa.domain.somoim;

import com.itda.moamoa.domain.participants.Participants;
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
    List<Participants> participants = new ArrayList<>();

    public Somoim(SomoimStatus status) {
        this.status = status;
    }
}
