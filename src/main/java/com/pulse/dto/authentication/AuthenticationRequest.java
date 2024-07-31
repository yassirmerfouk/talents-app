package com.pulse.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter
public class AuthenticationRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Email is not valid.")
    private String email;

    @NotEmpty(message = "Password is required.")
    @Length(min = 8, message = "Password min characters is 8.")
    private String password;
}
