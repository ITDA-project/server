package com.itda.moamoa.domain.participant.entity;

import com.itda.moamoa.domain.somoim.entity.Somoim;
import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity                                             // Entity
@Getter                                             // Getter 자동 생성
@Table(name = "participants")                       // Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 생성 - 같은 패키지/하위 클래스 내 접근 허용
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // 모든 필드 생성자 생성 - 내부 클래스만 접근 허용
@Builder                                            // Builder 제공
@ToString(exclude={"user, somoim"})                 // ToString Override - user, somoim 출력 제외
public class Participant {
    @Id                                                     // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // PK 자동 생성
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // Participant:User = n:1 - 지연 로딩
    @JoinColumn(name = "user_id")       // Foreign Key
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)  // Participant:Somoim = n:1 - 지연 로딩
    @JoinColumn(name = "somoim_id")
    private Somoim somoim;

    @Enumerated(EnumType.STRING)    // Enum Type
    private Role role;

    @Enumerated(EnumType.STRING)    // Enum Type
    private ParticipantStatus participantStatus;
}