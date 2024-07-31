package com.pulse.dto.experience;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ExperienceResponse {

    private Long id;
    private String title;
    private String company;
    private String country;
    private String city;
    private Integer yearOfStart;
    private Integer monthOfStart;
    private Integer yearOfEnd;
    private Integer monthOfEnd;
    private String description;
}
