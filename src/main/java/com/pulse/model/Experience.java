package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "experiences")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String company;
    private String country;
    private String city;
    @Column(length = 2000)
    private String description;
    private Integer yearOfStart;
    private Integer monthOfStart;
    private Integer yearOfEnd;
    private Integer monthOfEnd;

    @ManyToOne
    private Talent talent;

    public void copyProperties(Experience experience){
        title = experience.title;
        company = experience.company;
        country = experience.country;
        city = experience.city;
        yearOfStart = experience.yearOfStart;
        monthOfStart = experience.monthOfStart;
        yearOfEnd = experience.yearOfEnd;
        monthOfEnd = experience.monthOfEnd;
        description = experience.description;
    }
}
