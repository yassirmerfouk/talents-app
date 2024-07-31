package com.pulse.dto.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder
public abstract class RegistrationRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Email is not valid.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Length(min = 8, message = "Password min characters is 8.")
    private String password;

    @NotBlank(message = "Firstname is required.")
    private String firstName;

    @NotBlank(message = "Lastname is required.")
    private String lastName;

    @NotBlank(message = "Phone is required")
    private String phone;
}
