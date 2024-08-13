package com.pulse.dto.selection;

import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class SelectionRequest {

    private String title;
    private String description;
    private List<Long> talentsIds;
}
