package com.pulse.dto.job;

import com.pulse.dto.client.ClientResponse;
import com.pulse.enumeration.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class JobResponse {

    private Long id;
    private String title;
    private String sector;
    private String description;
    private Integer minSalary;
    private Integer maxSalary;
    private SalaryUnit salaryUnit;
    private Currency currency;
    private Integer yearsOfExperiences;
    private Integer numberOfTalents;
    private ContractType contractType;
    private Integer period;
    private PeriodUnit periodUnit;
    private JobType type;
    private JobStatus status;
    private LocalDateTime createdAt;
    private Set<String> skills;

    private boolean applied;

    private Long applicationsNumber;

    private ClientResponse client;

}
