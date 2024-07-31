package com.pulse.dto.suggestion;

import com.pulse.enumeration.ContractType;
import com.pulse.enumeration.JobType;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class SuggestedJob {

    private Long id;
    private String title;
    private ContractType contractType;
    private JobType jobType;
    private String shortDescription;
}
