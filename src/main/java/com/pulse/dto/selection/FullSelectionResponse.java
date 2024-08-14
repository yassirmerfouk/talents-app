package com.pulse.dto.selection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class FullSelectionResponse extends SelectionResponse{

    private List<ItemResponse> items;
}
