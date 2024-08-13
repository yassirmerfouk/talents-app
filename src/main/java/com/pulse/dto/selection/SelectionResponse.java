package com.pulse.dto.selection;

import com.pulse.enumeration.SelectionStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class SelectionResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private SelectionStatus status;

    private List<ItemResponse> items;
}
