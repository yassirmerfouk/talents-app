package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "educations")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String level;
    private String domain;
    private String university;
    private String country;
    private String city;
    private Integer yearOfStart;
    private Integer monthOfStart;
    private Integer yearOfEnd;
    private Integer monthOfEnd;

    @ManyToOne
    private Talent talent;

    public void copyProperties(Education education){
        level = education.level;
        domain = education.domain;
        university = education.university;
        country = education.country;
        city = education.city;
        yearOfStart = education.yearOfStart;
        monthOfStart = education.monthOfStart;
        yearOfEnd = education.yearOfEnd;
        monthOfEnd = education.monthOfEnd;
    }
}
