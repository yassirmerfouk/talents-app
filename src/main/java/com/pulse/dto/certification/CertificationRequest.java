package com.pulse.dto.certification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class CertificationRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    @Length(max = 1000, message = "Description max characters is 1000.")
    private String description;

    @PastOrPresent(message = "Date is not valid.")
    private LocalDate issuedAt;

    @URL(message = "Resource URL is not valid.")
    private String resource;
}
