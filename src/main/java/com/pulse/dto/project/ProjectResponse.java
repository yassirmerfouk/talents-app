package com.pulse.dto.project;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ProjectResponse {

    private Long id;
    private String title;
    private String shortDescription;
    private String longDescription;
    private String resource;
}
