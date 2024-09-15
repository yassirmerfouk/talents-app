package com.pulse.dto.interview;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class JobInterviewRequest {

    @Length(max = 1500, message = "Message max characters is 1500.")
    private String message;

    @NotNull(message = "First date is required.")
    @Future(message = "First date is not valid.")
    private LocalDateTime firstDate;

    @Future(message = "Second date is not valid.")
    private LocalDateTime secondDate;

    @Future(message = "Third date is not valid.")
    private LocalDateTime thirdDate;

}
