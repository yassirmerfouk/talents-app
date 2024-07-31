package com.pulse.dto.meet;

import com.pulse.enumeration.ContactType;
import com.pulse.enumeration.MeetType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class MeetRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotNull(message = "Date is required.")
    @FutureOrPresent(message = "Date is not valid.")
    private LocalDateTime date;

    @NotNull(message = "Meet type is required.")
    private MeetType meetType;

    @NotNull(message = "Contact type is required.")
    private ContactType contactType;

    @Length(max = 1500, message = "Max length is 1500.")
    private String firstBody;

    @Length(max = 1500, message = "Max length is 1500.")
    private String secondBody;

    @URL(message = "Resource is invalid.")
    private String resource;

    @NotNull(message = "Receivers are required.")
    @NotEmpty(message = "Receivers are required.")
    private List<Long> receivers;

    // IN CASE OF ADMIN_MEET
    private Long jobId;

    // IN CASE OF CLIENT_MEET
    private Long applicationId;

}
