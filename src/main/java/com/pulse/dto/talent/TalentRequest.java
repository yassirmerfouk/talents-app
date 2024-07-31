package com.pulse.dto.talent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class TalentRequest {

    @NotBlank(message = "email is required")
    @Email(message = "Email is not valid.")
    private String email;

    @NotBlank(message = "Firstname is required.")
    private String firstName;

    @NotBlank(message = "Lastname is required.")
    private String lastName;

    @NotBlank(message = "Phone is required.")
    private String phone;

    @NotBlank(message = "Title is required.")
    private String title;

    private String summary;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth is not valid.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "City is required.")
    private String city;

    private String address;

}
