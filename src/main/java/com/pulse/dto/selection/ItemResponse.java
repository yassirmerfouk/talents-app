package com.pulse.dto.selection;

import com.pulse.dto.talent.TalentResponse;
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

    private TalentResponse talent;
}
