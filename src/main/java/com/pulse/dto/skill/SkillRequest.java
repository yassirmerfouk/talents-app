package com.pulse.dto.skill;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class SkillRequest {

    @NotBlank(message = "Title is required.")
    private String title;
}
