package com.pulse.dto.suggestion;

import lombok.*;

import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Suggestion {

    private String firstName;
    private String lastName;
    private String email;

    private Set<SuggestedJob> suggestedJobs;

}
