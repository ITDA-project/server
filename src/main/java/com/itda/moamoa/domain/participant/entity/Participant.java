package com.itda.moamoa.domain.participant.entity;

import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude={"user, somoim"})
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // Participant:User = n:1 - 지연 로딩
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)  // Participant:Somoim = n:1 - 지연 로딩
    @JoinColumn(name = "somoim_id")
    private Somoim somoim;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus participantStatus;
}