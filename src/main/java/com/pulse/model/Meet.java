package com.pulse.model;

import com.pulse.enumeration.ContactType;
import com.pulse.enumeration.MeetStatus;
import com.pulse.enumeration.MeetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meets")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Meet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime date;
    @Enumerated(value = EnumType.STRING)
    private MeetType meetType;
    @Enumerated(value = EnumType.STRING)
    private ContactType contactType;
    @Enumerated(value = EnumType.STRING)
    private MeetStatus status;
    @Column(length = 1500)
    private String firstBody;
    @Column(length = 1500)
    private String secondBody;
    private String resource;

    @ManyToOne
    private User sender;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> receivers;
}
