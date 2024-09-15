package com.pulse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contracts")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt;

    @OneToOne
    private ClientContract clientContract;

    @OneToMany
    private List<TalentContract> talentContracts;
}
