package com.pulse.dto.reset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor @NoArgsConstructor @Setter @Getter @Builder
public class ResetPasswordRequest {

    @NotBlank(message = "Password is required.")
    @Length(min = 8, message = "Password min characters is 8.")
    private String password;

    @NotBlank(message = "RePassword is required.")
    @Length(min = 8, message = "RePassword min characters is 8.")
    private String rePassword;

    @NotNull(message = "Token is required.")
    private String token;
}
