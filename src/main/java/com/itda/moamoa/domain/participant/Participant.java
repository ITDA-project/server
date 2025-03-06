package com.itda.moamoa.domain.participant;
import com.itda.moamoa.domain.somoim.Somoim;
import com.itda.moamoa.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "participants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "somoim_id")
    private Somoim somoim;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus participantStatus;

}

