package com.pulse.dto.Education;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class EducationResponse {
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
}
