package com.pulse.dto.selection;

import com.pulse.dto.client.ClientResponse;
import com.pulse.enumeration.SelectionStatus;
import com.pulse.model.Client;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class SelectionResponse {

    private Long id;
    private String title;
    private String sector;
    private String description;
    private LocalDateTime createdAt;
    private SelectionStatus status;
    private Long numberOfTalents;

    private ClientResponse client;
}
