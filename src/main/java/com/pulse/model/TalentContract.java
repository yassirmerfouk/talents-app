package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "talent_contracts")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class TalentContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contractUrl;

    @ManyToOne
    private Talent talent;
}
