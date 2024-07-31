package com.pulse.dto.meet;

import com.pulse.dto.user.UserResponse;
import com.pulse.enumeration.ContactType;
import com.pulse.enumeration.MeetStatus;
import com.pulse.enumeration.MeetType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class MeetResponse {

    private Long id;
    private String title;
    private LocalDateTime date;
    private MeetType meetType;
    private ContactType contactType;
    private MeetStatus status;
    private String firstBody;
    private String secondBody;
    private String resource;

    private UserResponse sender;
    private List<UserResponse> receivers;
}
