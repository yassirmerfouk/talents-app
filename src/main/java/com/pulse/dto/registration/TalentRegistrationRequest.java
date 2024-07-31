package com.pulse.dto.registration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public class TalentRegistrationRequest extends RegistrationRequest{

    @NotBlank(message = "Title is required.")
    private String title;

    @Length(max = 255, message = "Summary max characters is 255.")
    private String summary;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth is not valid.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "City is required.")
    private String city;

    private String address;


}
