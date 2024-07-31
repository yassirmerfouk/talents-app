package com.pulse.dto.certification;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class CertificationResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate issuedAt;
    private String resource;
}
