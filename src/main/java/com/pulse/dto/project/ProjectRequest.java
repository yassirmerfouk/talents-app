package com.pulse.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ProjectRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    @Length(max = 255, message = "Short description max characters is 255.")
    private String shortDescription;

    @Length(max = 2000, message = "Short description max characters is 2000.")
    private String longDescription;

    @URL(message = "Resource is not valid.")
    private String resource;
}
