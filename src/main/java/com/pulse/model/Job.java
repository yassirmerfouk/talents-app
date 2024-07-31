package com.pulse.model;

import com.pulse.enumeration.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "jobs")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@EqualsAndHashCode(of = {"id"})
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String sector;
    @Column(length = 5000)
    private String description;
    private Integer minSalary;
    private Integer maxSalary;
    @Enumerated(EnumType.STRING)
    private SalaryUnit salaryUnit;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Integer yearsOfExperiences;
    private Integer numberOfTalents;
    @Enumerated(EnumType.STRING)
    private ContractType contractType;
    private Integer period;
    @Enumerated(EnumType.STRING)
    private PeriodUnit periodUnit;

    @Enumerated(EnumType.STRING)
    private JobType type;
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private LocalDateTime createdAt;

    @ManyToMany
    private Set<Skill> skills;

    @ManyToOne
    private Client client;

    public void copyProperties(Job job) {
        title = job.title;
        sector = job.sector;
        description = job.description;
        minSalary = job.minSalary;
        maxSalary = job.maxSalary;
        salaryUnit = job.salaryUnit;
        currency = job.currency;
        yearsOfExperiences = job.yearsOfExperiences;
        numberOfTalents = job.numberOfTalents;
        contractType = job.contractType;
        period = job.period;
        periodUnit = job.periodUnit;
        type = job.type;
    }

}
