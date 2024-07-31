package com.pulse.dto.client;

import com.pulse.enumeration.ClientType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ClientRequest {

    @NotBlank(message = "email is required.")
    @Email(message = "Email is not valid.")
    private String email;

    @NotBlank(message = "Firstname is required.")
    private String firstName;

    @NotBlank(message = "Lastname is required.")
    private String lastName;

    @NotBlank(message = "Phone is required.")
    private String phone;

    private String companyName;

    @NotBlank(message = "Sector is required.")
    private String sector;

    @Length(max = 255, message = "Short description max characters is 255.")
    private String shortDescription;

    @Length(max = 5000, message = "Long description max characters is 5000.")
    private String longDescription;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "Country is required.")
    private String country;

    @URL(message = "Website is not valid.")
    private String website;

    private String size;

    private ClientType type;
}
