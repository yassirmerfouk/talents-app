package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

}
