package com.pulse.dto.selection;

import com.pulse.dto.talent.TalentResponse;
import com.pulse.enumeration.SelectionStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ItemResponse {

    private Long id;
    private boolean selected;
    private String level;
    private String report;

    private SelectionStatus selectionStatus;

    private TalentResponse talent;
}
