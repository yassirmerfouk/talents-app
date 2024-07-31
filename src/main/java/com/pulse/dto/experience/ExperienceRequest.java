package com.pulse.dto.experience;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ExperienceRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Company is required.")
    private String company;

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

    @Length(max = 2000, message = "Description max characters is 2000")
    private String description;
}
