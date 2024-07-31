package com.pulse.dto.language;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class LanguageRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Level is required.")
    private String level;
}
