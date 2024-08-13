package com.pulse.dto.job;

import com.pulse.enumeration.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class JobRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Sector is required.")
    private String sector;

    @Length(max = 5000, message = "Description max length is 5000.")
    private String description;

    @NotNull(message = "Min Salary is required.")
    @Min(value = 0, message = "Min salary is not valid.")
    private Integer minSalary;

    @NotNull(message = "Max salary is required.")
    @Min(value = 0, message = "Max salary is not valid.")
    private Integer maxSalary;

    @NotNull(message = "Salary unit is required.")
    private SalaryUnit salaryUnit;

    @NotNull(message = "Currency is required.")
    private Currency currency;

    @NotNull(message = "Years of experiences is required.")
    private Integer yearsOfExperiences;

    @NotNull(message = "Number of talents is required.")
    private Integer numberOfTalents;

    @NotNull(message = "Contract type is required.")
    private ContractType contractType;

    private Integer period;

    private PeriodUnit periodUnit;


    @NotNull(message = "Type is required.")
    private JobType type;

    private Set<String> skills;
}
