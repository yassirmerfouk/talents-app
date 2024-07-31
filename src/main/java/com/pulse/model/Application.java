package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long experiencesScore;
    private Long projectsScore;
    private Long skillsScore;
    private Long score;
    private boolean selected;
    private boolean approved;
    private boolean refused;

    private boolean hasAdminMeet;
    private boolean hasClientMeet;

    @ManyToOne
    private Job job;

    @ManyToOne
    private Talent talent;
}
