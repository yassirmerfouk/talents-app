package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "certifications")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1000)
    private String description;
    private LocalDate issuedAt;
    private String resource;

    @ManyToOne
    private Talent talent;

    public void copyProperties(Certification certification){
        title = certification.title;
        description = certification.description;
        issuedAt = certification.issuedAt;
        resource = certification.resource;
    }
}
