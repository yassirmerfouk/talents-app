package com.pulse.dto.interview;

import com.pulse.enumeration.JobInterviewStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class JobInterviewResponse {

    private Long id;
    private String message;
    private LocalDateTime firstDate;
    private LocalDateTime secondDate;
    private LocalDateTime thirdDate;
    private JobInterviewStatus status;
}
