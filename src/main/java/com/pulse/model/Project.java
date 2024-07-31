package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "projects")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String shortDescription;
    @Column(length = 2000)
    private String longDescription;
    private String resource;

    @ManyToOne
    private Talent talent;

    public void copyProperties(Project project){
        title = project.title;
        shortDescription = project.shortDescription;
        longDescription = project.longDescription;
        resource = project.resource;
    }
}
