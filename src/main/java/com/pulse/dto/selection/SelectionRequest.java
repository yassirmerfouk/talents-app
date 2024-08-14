package com.pulse.dto.selection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class SelectionRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Sector is required.")
    private String sector;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotEmpty(message = "Talents ids are required.")
    private List<Long> talentsIds;
}
