package com.pulse.dto.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticResponse {

    private Long totalTalents;
    private Long totalEnabledTalents;
    private Long totalClients;
    private Long totalEnabledClients;
    private Long totalJobs;
    private Long totalSelections;
}
