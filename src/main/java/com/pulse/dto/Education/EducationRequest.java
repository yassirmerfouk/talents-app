package com.pulse.dto.Education;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class EducationRequest {

    @NotBlank(message = "Level is required.")
    private String level;

    @NotBlank(message = "Domain is required.")
    private String domain;

    @NotBlank(message = "University is required.")
    private String university;

    @NotBlank(message = "Country is required.")
    private String country;

    @NotBlank(message = "City is required.")
    private String city;

    @NotNull(message = "Year of start is required.")
    @Min(value = 1990, message = "Year of start is not valid.")
    private Integer yearOfStart;

    @NotNull(message = "Month of start is required.")
    @Min(value = 1, message = "Month of start is not valid.")
    @Max(value = 12, message = "Month of start is not valid.")
    private Integer monthOfStart;

    @Min(value = 1990, message = "Year of end is not valid.")
    private Integer yearOfEnd;

    @Min(value = 1, message = "Month of end is not valid.")
    @Max(value = 12, message = "Month of end is not valid.")
    private Integer monthOfEnd;

}
