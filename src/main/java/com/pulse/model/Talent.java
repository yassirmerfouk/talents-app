package com.pulse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "talents")
@DiscriminatorValue(value = "TALENT")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class Talent extends User{

    private String title;
    @Column(length = 3000)
    private String summary;
    private LocalDate dateOfBirth;
    private String city;
    private String address;
    private boolean available;

    @OneToMany(mappedBy = "talent")
    private List<Experience> experiences;

    @OneToMany(mappedBy = "talent")
    private List<Education> educations;

    @OneToMany(mappedBy = "talent")
    private List<Project> projects;

    @ManyToMany
    private Set<Skill> skills;

    @OneToMany(mappedBy = "talent")
    private List<Certification> certifications;

    @OneToMany(mappedBy = "talent")
    private List<Language> languages;

    public void copyProperties(Talent talent){
        super.copyProperties(talent);
        title = talent.title;
        summary = talent.summary;
        dateOfBirth = talent.dateOfBirth;
        city = talent.city;
        address = talent.address;
        available = talent.available;
    }
}
