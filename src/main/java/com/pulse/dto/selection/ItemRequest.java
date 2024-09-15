package com.pulse.dto.selection;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ItemRequest {

    private Long id;
    private String level;
    private String report;
}
