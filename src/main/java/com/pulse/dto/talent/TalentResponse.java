package com.pulse.dto.talent;

import com.pulse.enumeration.VerificationStatus;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class TalentResponse {

    protected Long id;
    protected String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String image;
    private VerificationStatus status;
    private String title;
    private String summary;
    private LocalDate dateOfBirth;
    private String city;
    private String address;
}
